# Streamlit Calculator App

A simple calculator web app built with Streamlit.

## Project structure

| File / folder | Purpose |
|---|---|
| `app.py` | Streamlit UI |
| `calculator.py` | Pure calculator logic (no UI dependency) |
| `tests/` | Pytest test suite |

## Setup

1. Create and activate a virtual environment (optional but recommended):
	- `python3 -m venv .venv`
	- `source .venv/bin/activate`
2. Install dependencies:
	- `pip install -r requirements.txt`

## Run

Start the app with:

`streamlit run app.py`

Then open the local URL shown in the terminal (usually `http://localhost:8501`).

## Test

Run the unit tests with:

`pytest`