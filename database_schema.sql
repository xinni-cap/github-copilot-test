-- ============================================================
-- Database Schema: Streamlit Calculator Application
-- Dialect:         PostgreSQL 14+
-- Generated from:  Code Analysis of app.py
-- Date:            2025-01-31
-- Description:     Production persistence layer for the
--                  Streamlit Calculator — tracks calculation
--                  history, user sessions, error events, and
--                  usage analytics for a currently stateless
--                  single-page calculator application.
-- ============================================================

-- ============================================================
-- SECTION 0 — HOUSEKEEPING
-- Enable UUID generation extension (PostgreSQL built-in).
-- ============================================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- ============================================================
-- Table: operations
-- Description: Lookup / reference table that normalises the
--              four valid arithmetic operations.  Prevents
--              free-text operation values in calculations and
--              provides the human-readable symbol and
--              description used in reporting views.
-- Business Rule: BR-005 — Exactly four operations are
--                supported (Add, Subtract, Multiply, Divide).
-- ============================================================
CREATE TABLE operations (
    operation_code  VARCHAR(10)  NOT NULL,
    operation_name  VARCHAR(50)  NOT NULL,
    symbol          VARCHAR(3)   NOT NULL,   -- Display symbol: +  -  ×  ÷
    description     VARCHAR(255),

    -- Primary key uses the natural business key (the string
    -- the application already enforces via selectbox).
    CONSTRAINT pk_operations PRIMARY KEY (operation_code),

    -- Symbol must be one of the four mathematical glyphs.
    CONSTRAINT chk_operations_symbol
        CHECK (symbol IN ('+', '-', '×', '÷')),

    -- Name must be non-empty.
    CONSTRAINT chk_operations_name_nonempty
        CHECK (LENGTH(TRIM(operation_name)) > 0)
);

COMMENT ON TABLE  operations                IS 'Reference table for the four supported arithmetic operations (BR-005).';
COMMENT ON COLUMN operations.operation_code IS 'Natural PK matching app selectbox values: Add | Subtract | Multiply | Divide.';
COMMENT ON COLUMN operations.operation_name IS 'Human-readable full name of the operation.';
COMMENT ON COLUMN operations.symbol         IS 'Mathematical display symbol used in formatted result equations (BR-006).';
COMMENT ON COLUMN operations.description    IS 'Plain-language description of the operation for documentation purposes.';

-- Seed the four valid operations (matches app.py selectbox options exactly).
INSERT INTO operations (operation_code, operation_name, symbol, description) VALUES
    ('Add',      'Addition',       '+', 'Adds first_number and second_number together.'),
    ('Subtract', 'Subtraction',    '-', 'Subtracts second_number from first_number.'),
    ('Multiply', 'Multiplication', '×', 'Multiplies first_number by second_number.'),
    ('Divide',   'Division',       '÷', 'Divides first_number by second_number. second_number must not be zero (BR-001).');


-- ============================================================
-- Table: sessions
-- Description: Tracks individual Streamlit user sessions.
--              A session begins the first time a user
--              submits the calculator form and is identified
--              by a UUID generated server-side (or passed via
--              Streamlit session_state).
--              calculation_count is denormalised for fast
--              dashboard queries; updated via trigger.
-- ============================================================
CREATE TABLE sessions (
    session_id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    started_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_active_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    calculation_count   INTEGER      NOT NULL DEFAULT 0,
    user_agent          VARCHAR(500),  -- Browser/client user-agent string (optional)
    ip_address          VARCHAR(45),   -- IPv4 or IPv6 address, nullable for privacy

    -- Constraints
    CONSTRAINT pk_sessions PRIMARY KEY (session_id),
    CONSTRAINT chk_sessions_calc_count
        CHECK (calculation_count >= 0),
    CONSTRAINT chk_sessions_last_active_gte_started
        CHECK (last_active_at >= started_at)
);

COMMENT ON TABLE  sessions                    IS 'Tracks individual calculator user sessions.';
COMMENT ON COLUMN sessions.session_id         IS 'UUID primary key — generated server-side and stored in Streamlit session_state.';
COMMENT ON COLUMN sessions.started_at         IS 'Timestamp when the session was first created (first form submission).';
COMMENT ON COLUMN sessions.last_active_at     IS 'Timestamp of the most recent calculation in this session; updated by trigger.';
COMMENT ON COLUMN sessions.calculation_count  IS 'Denormalised count of calculations submitted in this session; updated by trigger.';
COMMENT ON COLUMN sessions.user_agent         IS 'HTTP User-Agent string captured at session start for analytics (nullable).';
COMMENT ON COLUMN sessions.ip_address         IS 'Client IP address at session start, nullable to support privacy/GDPR requirements.';

-- Index: look up sessions by start time for time-range analytics.
CREATE INDEX idx_sessions_started_at    ON sessions (started_at);
CREATE INDEX idx_sessions_last_active   ON sessions (last_active_at);


-- ============================================================
-- Table: calculations
-- Description: Core fact table — persists every calculation
--              event submitted by any user session.
--              A row is written for BOTH successful results
--              and error events (e.g., division by zero).
-- Business Rules enforced:
--   BR-001  Division by zero → is_error=TRUE, result=NULL
--   BR-002  Inputs stored at NUMERIC(18,6) — 6 decimal places
--   BR-004  operation_code FK constrains to 4 valid values
--   BR-005  result derived from first_number op second_number
-- ============================================================
CREATE TABLE calculations (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    session_id      UUID         NOT NULL,

    -- Operands — stored at 6 decimal precision per BR-002.
    -- NUMERIC(18,6): up to 999,999,999,999.999999
    first_number    NUMERIC(18,6) NOT NULL,
    second_number   NUMERIC(18,6) NOT NULL,

    -- Operation — FK to operations lookup table (BR-004/BR-005).
    operation_code  VARCHAR(10)  NOT NULL,

    -- Result — NULL when is_error = TRUE (BR-001 division-by-zero).
    result          NUMERIC(18,6),

    -- Error tracking
    is_error        BOOLEAN      NOT NULL DEFAULT FALSE,
    error_message   VARCHAR(500),   -- Populated only when is_error = TRUE

    -- Audit timestamp
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Primary key
    CONSTRAINT pk_calculations PRIMARY KEY (id),

    -- Foreign keys
    CONSTRAINT fk_calculations_session
        FOREIGN KEY (session_id)
        REFERENCES sessions (session_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_calculations_operation
        FOREIGN KEY (operation_code)
        REFERENCES operations (operation_code)
        ON DELETE RESTRICT,

    -- Consistency: result must be NULL iff is_error is TRUE.
    -- Prevents storing a result alongside an error flag.
    CONSTRAINT chk_calculations_result_error_consistency
        CHECK (
            (is_error = FALSE AND result IS NOT NULL)
         OR (is_error = TRUE  AND result IS NULL)
        ),

    -- Error message must be present when is_error = TRUE.
    CONSTRAINT chk_calculations_error_message_required
        CHECK (
            (is_error = FALSE)
         OR (is_error = TRUE AND error_message IS NOT NULL AND LENGTH(TRIM(error_message)) > 0)
        ),

    -- BR-001: Division by zero — second_number = 0 only allowed
    -- when is_error = TRUE (the row records the failed attempt).
    CONSTRAINT chk_calculations_division_by_zero
        CHECK (
            NOT (operation_code = 'Divide' AND second_number = 0 AND is_error = FALSE)
        )
);

COMMENT ON TABLE  calculations                      IS 'Core fact table. Records every calculation event — both successes and errors — submitted through the calculator form.';
COMMENT ON COLUMN calculations.id                   IS 'UUID surrogate primary key generated server-side.';
COMMENT ON COLUMN calculations.session_id           IS 'FK to sessions. Groups calculations by user session.';
COMMENT ON COLUMN calculations.first_number         IS 'First operand entered by the user (BR-002: 6 decimal places, field name num1 in app.py).';
COMMENT ON COLUMN calculations.second_number        IS 'Second operand / divisor (BR-002: 6 decimal places, field name num2 in app.py). Must not be 0 for Divide unless is_error=TRUE (BR-001).';
COMMENT ON COLUMN calculations.operation_code       IS 'FK to operations.operation_code. Constrained to Add|Subtract|Multiply|Divide (BR-004).';
COMMENT ON COLUMN calculations.result               IS 'Computed arithmetic result. NULL when is_error=TRUE (e.g., division-by-zero attempt). BR-001/BR-005.';
COMMENT ON COLUMN calculations.is_error             IS 'TRUE if the calculation failed (e.g., division by zero). Paired with error_message (BR-001).';
COMMENT ON COLUMN calculations.error_message        IS 'Human-readable error description (e.g., "Division by zero is not allowed."). NULL when is_error=FALSE.';
COMMENT ON COLUMN calculations.created_at           IS 'UTC timestamp when the calculation event was persisted.';

-- Indexes for calculations table
-- FK indexes (critical for JOIN performance)
CREATE INDEX idx_calculations_session_id    ON calculations (session_id);
CREATE INDEX idx_calculations_operation     ON calculations (operation_code);

-- Query pattern: "show me all calculations in the last N days"
CREATE INDEX idx_calculations_created_at    ON calculations (created_at DESC);

-- Query pattern: "show only error rows" for error monitoring dashboards
CREATE INDEX idx_calculations_is_error      ON calculations (is_error)
    WHERE is_error = TRUE;   -- Partial index — small fraction of rows

-- Composite: "calculations by session ordered by time" (session history view)
CREATE INDEX idx_calculations_session_time  ON calculations (session_id, created_at DESC);

-- Composite: "error rate by operation" analytic query
CREATE INDEX idx_calculations_op_error      ON calculations (operation_code, is_error);


-- ============================================================
-- SECTION — TRIGGERS
-- ============================================================

-- ----------------------------------------------------------
-- Function: update_session_on_calculation
-- Keeps sessions.last_active_at and sessions.calculation_count
-- in sync every time a new calculation row is inserted.
-- ----------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_update_session_on_calculation()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE sessions
    SET
        last_active_at    = NEW.created_at,
        calculation_count = calculation_count + 1
    WHERE session_id = NEW.session_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger fires AFTER INSERT on calculations (not AFTER UPDATE/DELETE).
CREATE TRIGGER trg_calculations_update_session
    AFTER INSERT ON calculations
    FOR EACH ROW
    EXECUTE FUNCTION fn_update_session_on_calculation();

COMMENT ON FUNCTION fn_update_session_on_calculation() IS
    'Keeps sessions.last_active_at and calculation_count current after every new calculation insert.';


-- ============================================================
-- SECTION — VIEWS
-- ============================================================

-- ----------------------------------------------------------
-- View: v_calculation_summary
-- Purpose: Aggregated statistics per operation type.
--          Useful for usage analytics dashboards.
-- Columns:
--   operation_code     — operation identifier
--   operation_name     — human-readable name
--   symbol             — display symbol
--   total_calculations — total rows for this operation
--   successful_count   — rows where is_error = FALSE
--   error_count        — rows where is_error = TRUE
--   error_rate_pct     — percentage of calculations that errored
--   avg_first_number   — average value of first_number
--   avg_second_number  — average value of second_number
--   avg_result         — average result (NULLs excluded)
--   first_used_at      — earliest calculation timestamp
--   last_used_at       — most recent calculation timestamp
-- ----------------------------------------------------------
CREATE OR REPLACE VIEW v_calculation_summary AS
SELECT
    o.operation_code,
    o.operation_name,
    o.symbol,

    COUNT(c.id)                                                 AS total_calculations,
    COUNT(c.id) FILTER (WHERE c.is_error = FALSE)               AS successful_count,
    COUNT(c.id) FILTER (WHERE c.is_error = TRUE)                AS error_count,

    ROUND(
        COUNT(c.id) FILTER (WHERE c.is_error = TRUE)::NUMERIC
        / NULLIF(COUNT(c.id), 0) * 100,
        2
    )                                                           AS error_rate_pct,

    ROUND(AVG(c.first_number),  6)                              AS avg_first_number,
    ROUND(AVG(c.second_number), 6)                              AS avg_second_number,
    ROUND(AVG(c.result),        6)                              AS avg_result,

    MIN(c.created_at)                                           AS first_used_at,
    MAX(c.created_at)                                           AS last_used_at

FROM operations o
LEFT JOIN calculations c ON c.operation_code = o.operation_code
GROUP BY o.operation_code, o.operation_name, o.symbol
ORDER BY total_calculations DESC;

COMMENT ON VIEW v_calculation_summary IS
    'Aggregated usage and error statistics per arithmetic operation. LEFT JOIN ensures all four operations appear even with zero usage.';


-- ----------------------------------------------------------
-- View: v_recent_calculations
-- Purpose: Last 100 calculations enriched with operation
--          details — ready for a history / audit panel.
-- Columns:
--   id               — calculation UUID
--   session_id       — owning session UUID
--   first_number     — first operand
--   operation_symbol — display symbol (e.g. +)
--   second_number    — second operand
--   result           — computed result (NULL on error)
--   is_error         — error flag
--   error_message    — error text (NULL on success)
--   created_at       — event timestamp
--   operation_name   — full operation name
-- ----------------------------------------------------------
CREATE OR REPLACE VIEW v_recent_calculations AS
SELECT
    c.id,
    c.session_id,
    c.first_number,
    o.symbol                                    AS operation_symbol,
    c.second_number,
    c.result,
    c.is_error,
    c.error_message,
    c.created_at,
    o.operation_name
FROM calculations c
JOIN operations   o ON c.operation_code = o.operation_code
ORDER BY c.created_at DESC
LIMIT 100;

COMMENT ON VIEW v_recent_calculations IS
    'Last 100 calculation events enriched with operation display details. Designed for a real-time history panel in the UI.';


-- ============================================================
-- SECTION — TABLE-LEVEL COMMENTS (supplementary)
-- ============================================================
COMMENT ON TABLE operations   IS 'Reference / lookup table for the four supported arithmetic operations. Seeded at schema creation; should not be modified at runtime.';
COMMENT ON TABLE sessions      IS 'One row per Streamlit user session. Created on first form submit; updated by trigger on each subsequent calculation.';
COMMENT ON TABLE calculations  IS 'Fact table — one row per calculator form submission, whether successful or errored. Central table for all analytics and audit queries.';
