# Green Journey

Full project: web preview (React + Tailwind, installable PWA) and native Android app
(Kotlin + Jetpack Compose), both sharing the same dark aurora design and functionality.

- `greenjourney-android/` — native Android Studio project (Min SDK 26, Compose).
- `.github/workflows/build-apk.yml` — cloud CI that compiles **greenjourney.apk** and
  publishes it to the `apk-latest` release automatically on every push.

**Get the APK:** open the *Actions* tab → workflow runs on push → download from
Releases → `apk-latest`.
