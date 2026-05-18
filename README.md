# Streamlit Calculator App

A simple calculator web app built with Streamlit.

## Overview

This app provides a small calculator UI for two numeric inputs and four basic
operations:

- Add
- Subtract
- Multiply
- Divide

After each calculation, the app shows the formatted result and a
"Computation details" section with the submitted inputs, selected operation,
and computed output.

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

## Behavior Notes

- The calculator accepts decimal values for both inputs.
- Division by zero is blocked with an error message instead of returning a
  result.
- Results are only shown after clicking **Calculate**.
