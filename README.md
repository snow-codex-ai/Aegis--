# AEGIS Free V1

Android-first starter implementation for a free/local-first personal AI assistant.

## Build on GitHub Actions

The repository includes a ready-to-run workflow at `.github/workflows/build.yml`.
It installs Android SDK 35, uses JDK 17 and Gradle 8.10.2, builds the debug APK, and uploads it as the `AEGIS-debug` artifact.

Run it from **GitHub → Actions → Build AEGIS APK → Run workflow**.

## Included
- Futuristic dark Compose command center
- Multi-chat inbox foundation
- Tool matrix
- Market research dashboard foundation
- Custom assistant name
- Voice foreground-service foundation
- Permission-aware Android architecture
- Offline placeholder command router

## Important limitations
This is a starter V1, not a fully autonomous assistant. Full web search, local-model inference, persistent memory, document processing, speech recognition/TTS, device actions, and live market data still require additional implementations and permissions.

Trading features are research-oriented and do not guarantee outcomes. Live orders should always require explicit user confirmation.
