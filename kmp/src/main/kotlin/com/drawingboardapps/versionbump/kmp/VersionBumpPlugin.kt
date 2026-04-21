package com.drawingboardapps.versionbump.kmp

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class VersionBumpPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("versionBump", VersionBumpExtension::class.java)
        val commitCount = project.providers.of(GitCommitCountValueSource::class.java) {
            parameters.projectDir.set(project.rootDir)
        }
        val semverProvider = ext.semver.orElse(project.provider {
            throw GradleException(
                "kmpkit.version-bump: set versionBump { semver = \"x.y.z\" } on '${project.path}'",
            )
        })

        project.plugins.withId("com.android.application") {
            val components = project.extensions.getByType(
                ApplicationAndroidComponentsExtension::class.java,
            )
            components.onVariants { variant ->
                variant.outputs.forEach { output ->
                    output.versionCode.set(commitCount)
                    output.versionName.set(semverProvider)
                }
            }
        }

        project.afterEvaluate {
            ext.iosInfoPlist.orNull?.asFile?.let { plist ->
                applyIos(project, plist, semverProvider.get(), commitCount.get())
            }
        }
    }

    private fun applyIos(project: Project, plist: File, semver: String, commitCount: Int) {
        if (!plist.exists()) {
            project.logger.warn("[version-bump] iosInfoPlist not found: ${plist.absolutePath}")
            return
        }
        val current = plist.readText()
        val updated = rewritePlist(current, semver, commitCount.toString())
        if (updated != current) {
            plist.writeText(updated)
        }
        project.logger.lifecycle(
            "[version-bump] ${project.path}: ios CFBundleShortVersionString=$semver CFBundleVersion=$commitCount",
        )
    }
}
