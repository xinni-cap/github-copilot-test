CREATE TABLE calculator_test_cases (
    test_case_id BIGSERIAL PRIMARY KEY,
    test_name VARCHAR(100) NOT NULL UNIQUE,
    first_number NUMERIC(18,6) NOT NULL,
    second_number NUMERIC(18,6) NOT NULL,
    operation VARCHAR(10) NOT NULL,
    expected_result NUMERIC(28,10),
    expected_error VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_calculator_operation CHECK (operation IN ('Add', 'Subtract', 'Multiply', 'Divide')),
    CONSTRAINT chk_expected_outcome CHECK (
        (expected_result IS NOT NULL AND expected_error IS NULL)
        OR
        (expected_result IS NULL AND expected_error IS NOT NULL)
    )
);

INSERT INTO calculator_test_cases
    (test_name, first_number, second_number, operation, expected_result, expected_error)
VALUES
    ('add_basic', 12.5, 7.5, 'Add', 20.0, NULL),
    ('subtract_basic', 9.25, 4.25, 'Subtract', 5.0, NULL),
    ('multiply_basic', -3, 6, 'Multiply', -18.0, NULL),
    ('divide_basic', 22, 4, 'Divide', 5.5, NULL),
    ('divide_by_zero', 8, 0, 'Divide', NULL, 'Division by zero is not allowed.');
