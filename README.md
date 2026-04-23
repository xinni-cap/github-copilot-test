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

## Java Version

This project targets **Java 21 (LTS)** — the current long-term support release.

The required version is recorded in `.java-version` (read by [jenv](https://www.jenv.be/) and
[SDKMAN](https://sdkman.io/)) and is installed automatically by the
[Copilot setup-steps workflow](.github/workflows/copilot-setup-steps.yml) via the
[`actions/setup-java`](https://github.com/actions/setup-java) action using the
Eclipse Temurin distribution.

To install Java 21 locally with SDKMAN:

```bash
sdk install java 21-tem
```

Or with Homebrew (macOS/Linux):

```bash
brew install --cask temurin@21
```