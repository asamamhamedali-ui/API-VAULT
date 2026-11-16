# Security design

Current: API key values are encrypted using AES/GCM/NoPadding with a non-exportable Android Keystore AES key. Random IV per encryption. Only masked suffix is exposed in list UI.
Required before production: biometric/device credential lock, encrypted backup passphrase derivation, secret redaction, secure clipboard policy, key rotation/recovery UX, custom endpoint SSRF/redirect controls, threat modeling, and security testing.
Never paste real API keys into chat or commit them to Git.
