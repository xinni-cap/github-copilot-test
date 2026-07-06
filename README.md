# Streamlit Calculator App

A simple calculator web app built with Streamlit.

## Features

- Add, subtract, multiply, and divide two numbers
- Submit calculations from a single form-based UI
- Show the final equation and result in a success message
- Display computation details in an expandable panel
- Prevent division by zero with an inline error message

## Project structure

- `app.py` - Streamlit application entry point and calculator logic
- `requirements.txt` - Python dependency list

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

## How it works

1. Enter the first and second numbers.
2. Choose one of the supported operations: `Add`, `Subtract`, `Multiply`, or `Divide`.
3. Select **Calculate** to submit the form.
4. Review the result banner and expand **Computation details** to inspect the input and output values.

If you choose **Divide** and the second number is `0`, the app stops the calculation and shows an error instead of attempting the division.