require "fastlane/action"

module Fastlane
  module Actions
    class BumpInfoPlistAction < Action
      def self.run(params)
        plist = params[:plist]
        semver = params[:semver]

        UI.user_error!("plist not found: #{plist}") unless File.exist?(plist)

        build_num = `git rev-list --count HEAD`.strip
        UI.user_error!("git rev-list failed") if build_num.empty?

        plist_set(plist, "CFBundleVersion", build_num)
        if semver
          plist_set(plist, "CFBundleShortVersionString", semver)
          UI.success("[version-bump] #{plist}: #{semver} (#{build_num})")
        else
          UI.success("[version-bump] #{plist}: build=#{build_num} (semver unchanged)")
        end
      end

      def self.plist_set(plist, key, value)
        # :Add fails if the key exists; :Set fails if it doesn't. Try both,
        # fail only if the second also fails.
        system("/usr/libexec/PlistBuddy", "-c", "Add :#{key} string #{value}", plist,
               out: File::NULL, err: File::NULL)
        unless system("/usr/libexec/PlistBuddy", "-c", "Set :#{key} #{value}", plist)
          UI.user_error!("PlistBuddy failed to set #{key} in #{plist}")
        end
      end

      def self.description
        "Set CFBundleShortVersionString + CFBundleVersion in an Info.plist"
      end

      def self.details
        "CFBundleVersion is always set from `git rev-list --count HEAD` (monotonic; " \
          "App Store rejects duplicates). CFBundleShortVersionString is set from " \
          ":semver when provided; omitted ⇒ left untouched."
      end

      def self.available_options
        [
          FastlaneCore::ConfigItem.new(
            key: :plist,
            description: "Path to the Info.plist",
            optional: false,
          ),
          FastlaneCore::ConfigItem.new(
            key: :semver,
            description: "SemVer version string (e.g. 0.1.1). Omit to leave " \
              "CFBundleShortVersionString unchanged",
            optional: true,
          ),
        ]
      end

      def self.authors
        ["Drawing Board Apps LLC"]
      end

      def self.is_supported?(platform)
        platform == :ios
      end

      def self.example_code
        [
          'bump_info_plist(plist: "iosApp/Info.plist", semver: "0.1.1")',
          'bump_info_plist(plist: "iosApp/Info.plist")  # build-number-only',
        ]
      end

      def self.category
        :project
      end
    end
  end
end
