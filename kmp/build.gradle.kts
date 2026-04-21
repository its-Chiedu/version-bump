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

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website = "https://github.com/its-Chiedu/version-bump"
    vcsUrl = "https://github.com/its-Chiedu/version-bump.git"
    plugins {
        create("versionBump") {
            id = "com.drawingboardapps.versionbump.kmp"
            implementationClass = "com.drawingboardapps.versionbump.kmp.VersionBumpPlugin"
            displayName = "version-bump (KMP)"
            description = "Git-driven monotonic versionCode + SemVer versionName for Kotlin " +
                "Multiplatform apps. Sets Android versionCode/versionName and rewrites iOS " +
                "Info.plist (CFBundleShortVersionString + CFBundleVersion) from a single DSL block."
            tags = listOf("kotlin-multiplatform", "kmp", "versioning", "ios", "android", "git", "semver")
        }
    }
}
