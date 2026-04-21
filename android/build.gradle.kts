plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.7.3")
}

gradlePlugin {
    plugins {
        create("versionBump") {
            id = "com.drawingboardapps.versionbump.android"
            implementationClass = "com.drawingboardapps.versionbump.android.VersionBumpPlugin"
        }
    }
}
