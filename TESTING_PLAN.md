# Testing plan

Required: unit tests for encryption/pricing/routing/parsing; repository and Room integration tests; MockWebServer tests for provider adapters; Compose UI tests; end-to-end flow from key creation through a real provider request.
Failure cases: invalid key, missing permissions, rate limit, quota, timeout, malformed JSON, streaming interruption, duplicate taps, uncertain request completion, corrupt backup, migration failure, storage full, process death.
