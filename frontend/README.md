# Bank assistant web client

Start account-service on port 8091, transaction-service on 8092 (configured to call account-service on 8091), and chatbot-service on 8093 with `OPENAI_API_KEY` set. Start the banking services before chatbot-service so it can discover their MCP tools.

From this directory:

```bash
npm install
npm start
```

Visit http://localhost:4200. The Angular dev proxy forwards `/accounts` to port 8091 and `/chat` to port 8093. The OpenAI key stays on chatbot-service, never in the browser.
