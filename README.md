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

## Test insights

- Add: `2` and `3` should return `5`
- Subtract: `7` and `2` should return `5`
- Multiply: `4` and `2.5` should return `10`
- Divide: `9` and `3` should return `3`
- Divide by zero should show `Division by zero is not allowed.`
