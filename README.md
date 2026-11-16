# API VAULT — Android source project

This repository is an Android/Kotlin project foundation for API VAULT.

## Open and build
1. Install Android Studio (stable) and Android SDK Platform 35.
2. Open this folder in Android Studio.
3. Allow Gradle Sync to complete.
4. Build > Build Bundle(s) / APK(s) > Build APK(s).

Command line (with Android SDK and Gradle installed):
`gradle :app:assembleDebug`

## Current implementation status
This package includes a runnable Android application shell, Arabic navigation, a local API-key vault encrypted with Android Keystore AES-GCM, and a broad feature navigation map.

**It is not yet the completed product described in the full requirements.** Provider adapters, unified chat, custom provider/model execution, real usage/balance retrieval, smart routing, encrypted export/restore, and full automated test coverage still require implementation and live verification. The UI labels do not claim those integrations are complete.

## Security notes
- Do not commit real API keys, signing keys, passwords, or local configuration secrets.
- The key vault stores ciphertext and IV in app-private SharedPreferences; the encryption key is generated in Android Keystore.
- This starter does not provide cloud sync.
- Use HTTPS only. Do not send credentials to an untrusted custom endpoint.
- Key encryption does not protect against a fully compromised/rooted device or malicious code running inside the app.

## Package
`com.apivault`
