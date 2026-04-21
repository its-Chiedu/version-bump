package com.drawingboardapps.versionbump.kmp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PlistRewriteTest {

    private val standardPlist = """
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
        <plist version="1.0">
        <dict>
            <key>CFBundleShortVersionString</key>
            <string>0.0.0</string>
            <key>CFBundleVersion</key>
            <string>0</string>
            <key>CFBundleName</key>
            <string>SampleApp</string>
        </dict>
        </plist>
    """.trimIndent()

    @Test
    fun `rewrites both version keys`() {
        val out = rewritePlist(standardPlist, semver = "1.2.3", buildNumber = "42")
        assertTrue(out.contains("<key>CFBundleShortVersionString</key>\n    <string>1.2.3</string>"))
        assertTrue(out.contains("<key>CFBundleVersion</key>\n    <string>42</string>"))
    }

    @Test
    fun `leaves unrelated keys untouched`() {
        val out = rewritePlist(standardPlist, semver = "1.2.3", buildNumber = "42")
        assertTrue(out.contains("<key>CFBundleName</key>\n    <string>SampleApp</string>"))
    }

    @Test
    fun `silently skips when key is absent`() {
        val missingShort = """
            <dict>
                <key>CFBundleVersion</key>
                <string>0</string>
            </dict>
        """.trimIndent()
        val out = rewritePlist(missingShort, semver = "9.9.9", buildNumber = "7")
        assertFalse(out.contains("9.9.9"))
        assertTrue(out.contains("<string>7</string>"))
    }

    @Test
    fun `is idempotent when values already match`() {
        val first = rewritePlist(standardPlist, semver = "1.2.3", buildNumber = "42")
        val second = rewritePlist(first, semver = "1.2.3", buildNumber = "42")
        assertEquals(first, second)
    }

    @Test
    fun `replaces values with embedded XML-special characters safely`() {
        val plistWithAmp = """
            <dict>
                <key>CFBundleShortVersionString</key>
                <string>0&amp;0</string>
                <key>CFBundleVersion</key>
                <string>0</string>
            </dict>
        """.trimIndent()
        val out = rewritePlist(plistWithAmp, semver = "1.0.0", buildNumber = "5")
        assertTrue(out.contains("<string>1.0.0</string>"))
        assertFalse(out.contains("0&amp;0"))
    }
}
