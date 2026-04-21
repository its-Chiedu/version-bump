lib = File.expand_path("lib", __dir__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require "fastlane/plugin/version_bump/version"

Gem::Specification.new do |spec|
  spec.name          = "fastlane-plugin-version_bump"
  spec.version       = Fastlane::VersionBump::VERSION
  spec.authors       = ["Drawing Board Apps LLC"]
  spec.email         = ["its-Chiedu@users.noreply.github.com"]

  spec.summary       = "Set CFBundleShortVersionString + CFBundleVersion from git + SemVer"
  spec.description   = "Fastlane action that writes iOS Info.plist versions: " \
                       "CFBundleVersion from `git rev-list --count HEAD` (monotonic) " \
                       "and CFBundleShortVersionString from a SemVer string."
  spec.homepage      = "https://github.com/its-Chiedu/version-bump"
  spec.license       = "MIT"

  spec.files         = Dir["lib/**/*", "README.md", "LICENSE"]
  spec.require_paths = ["lib"]

  spec.required_ruby_version = ">= 2.6"

  spec.add_dependency "fastlane", ">= 2.200.0"

  spec.add_development_dependency "rspec", "~> 3.12"
  spec.add_development_dependency "rake", "~> 13.0"
end
