package com.drawingboardapps.versionbump.kmp

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

abstract class VersionBumpExtension {
    abstract val semver: Property<String>
    abstract val iosInfoPlist: RegularFileProperty
}
