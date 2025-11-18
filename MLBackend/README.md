# ML Backend

Simple Flask API scaffold for future ML inference logic.

## Setup

```powershell
cd MLBackend
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
```

### Database Setup

Chat history is stored in PostgreSQL. Configure the database connection using one of the following methods:

**Option 1: Using DATABASE_URL**
```powershell
$env:DATABASE_URL = "postgresql://user:password@localhost:5432/instagram_chat"
```

**Option 2: Using individual environment variables**
```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "instagram_chat"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_password"
```

The database tables will be created automatically when the application starts.

## Run

```powershell
flask --app app run --host 0.0.0.0 --port 5001
```

The server now includes:

- `/health` – readiness probe
- `/predict` – placeholder inference endpoint (`_dummy_model`)
- `/chat` – LangChain-powered chatbot (requires a valid `GEMINI_API_KEY`)
  - Automatically saves and retrieves chat history from PostgreSQL
  - Accepts optional `session_id` parameter to maintain separate conversations
- `/chat/history/<session_id>` – Retrieve chat history for a specific session (optional `?limit=N` query parameter)

Set your Google Gemini API key before starting the server:

```powershell
$env:GEMINI_API_KEY = "<your key>"
flask --app app run
```

### Chat API Usage

**Basic chat request:**
```json
POST /chat
{
  "message": "Hello",
  "session_id": "user123"
}
```

If `session_id` is provided, the API will automatically load previous chat history from the database. If not provided, it defaults to "default". You can still pass `history` in the request body to override the database history.

**Response:**
```json
{
  "reply": "Hello! How can I help you?",
  "history": [
    {"role": "user", "content": "Hello"},
    {"role": "assistant", "content": "Hello! How can I help you?"}
  ]
}
```

**Retrieve chat history:**
```json
GET /chat/history/user123?limit=10
```

