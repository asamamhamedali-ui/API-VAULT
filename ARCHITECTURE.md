# Architecture plan

Target flow: Compose UI -> ViewModel -> UseCase -> Repository -> Provider Adapter -> HTTPS API.
Local persistence target: Room for structured metadata and usage history; Android Keystore for encryption key material.
Networking target: OkHttp with provider-specific adapters, strict timeouts, cancellation, redacted logging, and HTTPS-only endpoints.
Security: no secrets in source control, logs, crash reports, or plain exports. Custom endpoints require validation and explicit trust confirmation.
