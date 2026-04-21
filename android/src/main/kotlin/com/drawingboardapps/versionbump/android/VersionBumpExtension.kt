package com.drawingboardapps.versionbump.android

import org.gradle.api.provider.Property

abstract class VersionBumpExtension {
    abstract val semver: Property<String>
}
