# fastlane-plugin-version_bump

iOS Fastlane action that writes `CFBundleShortVersionString` + `CFBundleVersion` in an `Info.plist`.

`CFBundleVersion` always comes from `git rev-list --count HEAD` (monotonic — App Store rejects duplicates). `CFBundleShortVersionString` comes from a SemVer string you pass in; omit it to bump only the build number between rapid Firebase App Distribution iterations.

## Install

```bash
bundle add fastlane-plugin-version_bump --group=development
# or
fastlane add_plugin version_bump
```

Once published to RubyGems. Local-dev via gemspec path works too:

```ruby
# Gemfile
gem "fastlane-plugin-version_bump", path: "../version-bump/ios"
```

## Use

```ruby
# Fastfile
lane :beta do
  bump_info_plist(plist: "iosApp/Info.plist", semver: "0.1.1")
  build_app(...)
end
```

Omit `:semver` to bump build only:

```ruby
bump_info_plist(plist: "iosApp/Info.plist")
```

## Test

```bash
bundle install
bundle exec rspec
```

## Status

Alpha (`0.1.0.alpha1`). Uses `/usr/libexec/PlistBuddy` — macOS-only. Linux CI runners cannot execute this action (but they shouldn't be building iOS apps anyway).

## License

MIT — see [`../LICENSE`](../LICENSE). Copyright © 2026 Drawing Board Apps LLC.
