# Provider integration plan

Implement one adapter per provider, based on its current official documentation. Candidate providers: OpenAI, Google Gemini, Anthropic Claude, DeepSeek, Mistral, Groq, OpenRouter, xAI, Cohere, Together AI, Fireworks AI, Perplexity, and user-defined providers.
Do not assume all providers share the same authentication, request schema, streaming protocol, model discovery, usage fields, or billing endpoints. OpenAI-compatible support is a compatibility mode, not universal support.
Balance APIs may be unavailable or restricted for some providers; display “Unavailable” rather than inventing values.
