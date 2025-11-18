"""Minimal Flask API for the ML backend layer."""

from __future__ import annotations

import os
from typing import Any, Dict, List

from dotenv import load_dotenv
from flask import Flask, jsonify, request

from chatbot import ChatBot, MissingAPIKeyError
from chat_history import ChatHistoryDB

# Load environment variables from .env file
load_dotenv()

# Initialize database connection
try:
    db = ChatHistoryDB()
    db.init_db()  # Create tables if they don't exist
except Exception as e:
    print(f"Warning: Database initialization failed: {e}")
    print("Database features will be unavailable. Make sure DATABASE_URL or DB_* environment variables are set.")
    db = None


def create_app() -> Flask:
    """Application factory used by both the CLI and WSGI servers."""
    app = Flask(__name__)

    @app.get("/health")
    def health_check():
        """Simple readiness endpoint."""
        return jsonify({"status": "ok"}), 200

    @app.post("/predict")
    def predict():
        """Mock prediction endpoint demonstrating request/response flow."""
        payload: Dict[str, Any] = request.get_json(silent=True) or {}
        inputs: List[Any] | None = payload.get("inputs")

        if not inputs:
            return (
                jsonify(
                    {
                        "error": "Request JSON must include non-empty 'inputs' list.",
                    }
                ),
                400,
            )

        predictions = _dummy_model(inputs)
        return jsonify({"predictions": predictions}), 200

    @app.post("/chat")
    def chat():
        """Conversational endpoint backed by a LangChain pipeline."""
        payload = request.get_json(silent=True) or {}
        message = (payload.get("message") or "").strip()
        session_id = payload.get("session_id", "default")  # Default session if not provided

        if not message:
            return jsonify({"error": "Request JSON must include 'message'."}), 400

        try:
            # Load history from database (empty list if db not available)
            history = db.get_chat_history(session_id) if db else []

            bot = ChatBot()
            reply, updated_history = bot.reply(message=message, history=history)

            # Save to database if available
            if db:
                try:
                    # Save user message
                    db.save_message(session_id, "user", message)
                    # Save assistant reply
                    db.save_message(session_id, "assistant", reply)
                except Exception as db_exc:
                    app.logger.warning(f"Failed to save chat history: {db_exc}")
        except MissingAPIKeyError as exc:
            return jsonify({"error": str(exc), "hint": "Set GEMINI_API_KEY in .env file"}), 500
        except Exception as exc:  # pragma: no cover - defensive logging
            app.logger.exception("Chatbot inference failed: %s", exc)
            return jsonify({"error": "Chatbot inference failed"}), 500

        return jsonify({"reply": reply, "history": updated_history}), 200

    @app.get("/chat/history/<session_id>")
    def get_chat_history(session_id: str):
        """Retrieve chat history for a session from the database."""
        if not db:
            return jsonify({"error": "Database not available"}), 503

        try:
            limit = request.args.get("limit", type=int)
            history = db.get_chat_history(session_id, limit=limit)
            return jsonify({"session_id": session_id, "history": history}), 200
        except Exception as exc:
            app.logger.exception("Failed to retrieve chat history: %s", exc)
            return jsonify({"error": "Failed to retrieve chat history"}), 500

    return app


def _dummy_model(inputs: List[Any]) -> List[float]:
    """Placeholder model logic - replace with real inference later."""
    return [float(len(str(item))) for item in inputs]


if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5001))
    debug = os.environ.get("FLASK_DEBUG", "0") == "1"

    create_app().run(host="0.0.0.0", port=port, debug=debug)

