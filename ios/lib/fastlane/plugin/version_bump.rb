require "fastlane/plugin/version_bump/version"

module Fastlane
  module VersionBump
    def self.all_classes
      Dir[File.expand_path("**/{actions,helper}/*.rb", __dir__)]
    end
  end
end

Fastlane::VersionBump.all_classes.each { |file| require file }
