package com.drawingboardapps.versionbump.android

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionBumpPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("versionBump", VersionBumpExtension::class.java)
        val commitCount = project.providers.of(GitCommitCountValueSource::class.java) {
            parameters.projectDir.set(project.rootDir)
        }

        project.plugins.withId("com.android.application") {
            val components = project.extensions.getByType(
                ApplicationAndroidComponentsExtension::class.java,
            )
            components.onVariants { variant ->
                val semverProvider = ext.semver.orElse(project.provider {
                    throw GradleException(
                        "version-bump: set versionBump { semver = \"x.y.z\" } on '${project.path}'",
                    )
                })
                variant.outputs.forEach { output ->
                    output.versionCode.set(commitCount)
                    output.versionName.set(semverProvider)
                }
            }
        }
    }
}
