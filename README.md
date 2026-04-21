# version-bump

Monotonic, git-driven version bumping for Android, iOS, and Kotlin Multiplatform apps. Three artifacts from one repo — pick the one that matches your project.

| Project type | Use |
|---|---|
| Android-only (Gradle) | [`android/`](android/) — Gradle plugin |
| iOS-only (Fastlane) | [`ios/`](ios/) — Fastlane plugin gem |
| KMP / Compose Multiplatform | [`kmp/`](kmp/) — Gradle plugin (writes both Android + iOS Info.plist) |

## Why

`versionCode` / `CFBundleVersion` must be monotonic — stores reject duplicates. Hand-bumping is a pre-release foot-gun. This plugin wires both to `git rev-list --count HEAD` so every commit produces a distinct, ordered build number for free. `versionName` / `CFBundleShortVersionString` stay manual (SemVer) so they reflect intent, not mechanical progress.

## Usage (KMP quick-start)

```kotlin
// settings.gradle.kts
pluginManagement {
    includeBuild("../version-bump/kmp")        // local dev
    // or, once published:
    // plugins { id("com.drawingboardapps.versionbump.kmp") version "0.1.0-alpha01" }
}

// app-module/build.gradle.kts
plugins {
    id("com.drawingboardapps.versionbump.kmp")
}

versionBump {
    semver = "0.1.1"
    iosInfoPlist = rootProject.file("iosApp/Info.plist")
}
```

See each subdirectory's README for platform-specific details.

## Status

**Alpha.** API may change. Pinned version scheme: `0.1.0-alpha01` → `0.1.0` once tests + dogfooding stabilize.

Pre-alpha caveats:
- Info.plist rewrite uses a targeted regex on `CFBundleShortVersionString` and `CFBundleVersion` entries. Works on standard Xcode-generated plists; unusual formatting may not match.
- Requires `git` on `PATH` at Gradle configuration time. Falls back to `versionCode = 1` with a warning if unavailable (fresh clones / CI without fetch-depth can trip this).
- AGP version compiled against: 8.7.3. Should work on 8.x; not yet tested on 9.x.

## Contributing / issues

Source: `<github-url-tbd>`. License: MIT (see [`LICENSE`](LICENSE)). Copyright © 2026 Drawing Board Apps LLC.

## Related

- [`app-kit`](../app-kit/) — Drawing Board Apps' private parent repo that consumes this (and where other, not-yet-extracted tooling lives).
