"""LangChain-powered chatbot helper."""

from __future__ import annotations

import os
from dataclasses import dataclass
from typing import Dict, List, Sequence

from langchain_core.messages import AIMessage, BaseMessage, HumanMessage, SystemMessage
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_google_genai import ChatGoogleGenerativeAI


class MissingAPIKeyError(RuntimeError):
    """Raised when no Gemini API key is available for the chatbot."""


SYSTEM_PROMPT = (
    "You are a concise, friendly assistant for an Instagram-style app. "
    "Provide practical answers about product ideas, ML models, and general questions. "
    "Cite assumptions when relevant and keep responses under 150 words unless asked."
)


@dataclass(slots=True)
class ChatBotConfig:
    model_name: str = "gemini-2.5-flash"
    temperature: float = 0.2
    max_output_tokens: int | None = None
    api_key_env: str = "GEMINI_API_KEY"


class ChatBot:
    """Thin wrapper around a LangChain Runnable powering responses."""

    def __init__(self, *, config: ChatBotConfig | None = None):
        self.config = config or ChatBotConfig()
        api_key = os.environ.get(self.config.api_key_env)
        if not api_key:
            raise MissingAPIKeyError(
                f"Missing {self.config.api_key_env}. Set it to a valid Google Gemini API key."
            )

        self.prompt = ChatPromptTemplate.from_messages(
            [
                ("system", SYSTEM_PROMPT),
                MessagesPlaceholder("history"),
                ("human", "{user_message}"),
            ]
        )

        llm = ChatGoogleGenerativeAI(
            google_api_key=api_key,
            model=self.config.model_name,
            temperature=self.config.temperature,
            max_output_tokens=self.config.max_output_tokens,
        )

        self.chain = self.prompt | llm | StrOutputParser()

    def reply(
        self,
        *,
        message: str,
        history: Sequence[Dict[str, str]] | None = None,
    ) -> tuple[str, list[dict[str, str]]]:
        history_docs = self._convert_history(history)
        response = self.chain.invoke({"history": history_docs, "user_message": message})

        updated_history = list(history or [])
        updated_history.append({"role": "user", "content": message})
        updated_history.append({"role": "assistant", "content": response})
        return response, updated_history

    @staticmethod
    def _convert_history(
        history: Sequence[Dict[str, str]] | None,
    ) -> List[BaseMessage]:
        records = history or []
        messages: List[BaseMessage] = []
        for record in records:
            role = (record.get("role") or "").lower()
            content = record.get("content") or ""
            if not content:
                continue
            if role in {"user", "human"}:
                messages.append(HumanMessage(content=content))
            elif role in {"assistant", "ai"}:
                messages.append(AIMessage(content=content))
            elif role == "system":
                messages.append(SystemMessage(content=content))
        return messages


def main() -> None:
    """Test the chatbot with dummy data."""
    from dotenv import load_dotenv

    # Load environment variables from .env file
    load_dotenv()

    print("Initializing chatbot...")
    try:
        bot = ChatBot()
        print("✓ Chatbot initialized successfully\n")
    except MissingAPIKeyError as e:
        print(f"✗ Error: {e}")
        print("\nPlease set GEMINI_API_KEY in your .env file or environment variables.")
        return

    # Dummy conversation history
    dummy_history = [
        {"role": "user", "content": "What is machine learning?"},
        {"role": "assistant", "content": "Machine learning is a subset of AI that enables systems to learn from data."},
    ]

    # Test messages
    test_messages = [
        "How can I use ML for image recognition?",
        "What are some popular ML frameworks?",
    ]

    print("=" * 60)
    print("Testing Chatbot with Dummy Data")
    print("=" * 60)
    print(f"\nInitial history: {len(dummy_history)} messages\n")

    current_history = dummy_history.copy()

    for i, message in enumerate(test_messages, 1):
        print(f"\n--- Test Message {i} ---")
        print(f"User: {message}")
        print("\nProcessing...")

        try:
            reply, updated_history = bot.reply(message=message, history=current_history)
            print(f"\nAssistant: {reply}")
            print(f"\nUpdated history length: {len(updated_history)} messages")
            current_history = updated_history
        except Exception as e:
            print(f"\n✗ Error: {e}")
            break

    print("\n" + "=" * 60)
    print("Test completed!")
    print("=" * 60)


if __name__ == "__main__":
    main()
