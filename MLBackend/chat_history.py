"""PostgreSQL database module for storing chat history."""

from __future__ import annotations

import os
from datetime import datetime
from typing import List, Optional

from sqlalchemy import Column, DateTime, Integer, String, Text, create_engine
from sqlalchemy.orm import DeclarativeBase, sessionmaker


class Base(DeclarativeBase):
    """Base class for declarative models."""
    pass


class ChatMessage(Base):
    """Chat message model for storing conversation history."""

    __tablename__ = "chat_messages"

    id = Column(Integer, primary_key=True, autoincrement=True)
    session_id = Column(String(255), nullable=False, index=True)
    role = Column(String(50), nullable=False)  # 'user' or 'assistant'
    content = Column(Text, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    def to_dict(self) -> dict[str, str]:
        """Convert message to dictionary format compatible with chatbot."""
        return {"role": self.role, "content": self.content}


class ChatHistoryDB:
    """Database handler for chat history operations."""

    def __init__(self, database_url: Optional[str] = None):
        """Initialize database connection.

        Args:
            database_url: PostgreSQL connection string. If None, reads from
                         DATABASE_URL environment variable or individual DB_* vars.
        """
        if database_url is None:
            database_url = os.environ.get("DATABASE_URL")
            if not database_url:
                # Use individual components if DATABASE_URL not set
                db_host = os.environ.get("DB_HOST", "localhost")
                db_port = os.environ.get("DB_PORT", "5432")
                db_name = os.environ.get("DB_NAME", "instagram_chat")
                db_user = os.environ.get("DB_USER", "postgres")
                db_password = os.environ.get("DB_PASSWORD", "")
                database_url = (
                    f"postgresql://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}"
                )

        self.engine = create_engine(database_url, pool_pre_ping=True)
        self.SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=self.engine)
        self.session = self.SessionLocal()

    def close(self):
        """Close the database session and connection."""
        if self.session:
            self.session.close()
        if self.engine:
            self.engine.dispose()

    def init_db(self) -> None:
        """Create all database tables."""
        Base.metadata.create_all(bind=self.engine)

    def save_message(self, session_id: str, role: str, content: str) -> None:
        """Save a chat message to the database.

        Args:
            session_id: Unique identifier for the chat session
            role: Message role ('user' or 'assistant')
            content: Message content
        """
        try:
            message = ChatMessage(
                session_id=session_id,
                role=role,
                content=content,
            )
            self.session.add(message)
            self.session.commit()
        except Exception:
            self.session.rollback()
            raise

    def get_chat_history(
        self, session_id: str, limit: Optional[int] = None
    ) -> List[dict[str, str]]:
        """Retrieve chat history for a session.

        Args:
            session_id: Unique identifier for the chat session
            limit: Optional limit on number of messages to retrieve

        Returns:
            List of message dictionaries in format [{"role": "...", "content": "..."}, ...]
        """
        query = (
            self.session.query(ChatMessage)
            .filter(ChatMessage.session_id == session_id)
            .order_by(ChatMessage.created_at.asc())
        )

        if limit:
            query = query.limit(limit)

        messages = query.all()
        return [msg.to_dict() for msg in messages]
