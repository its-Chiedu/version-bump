package com.drawingboardapps.versionbump.kmp

/**
 * Rewrites an Info.plist XML string, replacing the values of
 * `CFBundleShortVersionString` and `CFBundleVersion` in-place. Other content is
 * untouched. If a key is absent from the plist, that key is silently skipped
 * — we don't inject missing keys, since a missing key usually means the
 * plist isn't a standard iOS bundle plist and we shouldn't guess where to add.
 *
 * Implementation intentionally stays regex-based on these two specific keys
 * rather than parsing the plist as XML. Xcode-generated plists are stable in
 * format, and pulling in a DOM parser adds deps without real robustness gain
 * for this narrow use case.
 */
internal fun rewritePlist(source: String, semver: String, buildNumber: String): String {
    var out = source
    out = out.replace(plistKeyRegex("CFBundleShortVersionString"), "$1$semver$2")
    out = out.replace(plistKeyRegex("CFBundleVersion"), "$1$buildNumber$2")
    return out
}

// $1 captures everything up to and including the opening <string>,
// $2 the closing </string>. Matches across whitespace between tags.
private fun plistKeyRegex(key: String): Regex = Regex(
    "(<key>${Regex.escape(key)}</key>\\s*<string>)[^<]*(</string>)",
)
