# Streamlit Calculator App

A simple calculator web app built with Streamlit.

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

## Summit insights

- Supports four operations: Add, Subtract, Multiply, and Divide.
- Prevents division by zero and shows an error instead of crashing.
- Shows a "Computation details" panel with input values, selected operation, and result.