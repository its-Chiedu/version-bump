plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
}

group = "com.drawingboardapps.versionbump"
version = "0.1.0-alpha01"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.7.3")
}

gradlePlugin {
    website = "https://github.com/its-Chiedu/version-bump"
    vcsUrl = "https://github.com/its-Chiedu/version-bump.git"
    plugins {
        create("versionBump") {
            id = "com.drawingboardapps.versionbump.android"
            implementationClass = "com.drawingboardapps.versionbump.android.VersionBumpPlugin"
            displayName = "version-bump (Android)"
            description = "Git-driven monotonic versionCode + SemVer versionName for Android apps. " +
                "Reads `git rev-list --count HEAD` for versionCode; takes a SemVer string for versionName."
            tags = listOf("android", "versioning", "git", "semver", "versioncode")
        }
    }
}
