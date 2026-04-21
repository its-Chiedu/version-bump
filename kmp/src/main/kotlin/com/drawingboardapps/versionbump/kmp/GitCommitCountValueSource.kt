package com.drawingboardapps.versionbump.kmp

import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

/**
 * Configuration-cache-safe git commit-count lookup. Gradle tracks the exec
 * as a cache input so re-configuration only reruns when the result would
 * change.
 */
abstract class GitCommitCountValueSource :
    ValueSource<Int, GitCommitCountValueSource.Params> {

    interface Params : ValueSourceParameters {
        val projectDir: Property<File>
    }

    @get:Inject
    abstract val execOps: ExecOperations

    override fun obtain(): Int {
        val stdout = ByteArrayOutputStream()
        val result = execOps.exec {
            commandLine("git", "rev-list", "--count", "HEAD")
            workingDir = parameters.projectDir.get()
            standardOutput = stdout
            errorOutput = ByteArrayOutputStream()
            isIgnoreExitValue = true
        }
        if (result.exitValue != 0) return 1
        return stdout.toString(Charsets.UTF_8).trim().toIntOrNull() ?: 1
    }
}
