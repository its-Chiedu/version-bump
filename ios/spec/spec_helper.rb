require "fastlane"
require "fastlane/plugin/version_bump"

RSpec.configure do |config|
  config.expect_with :rspec do |c|
    c.syntax = :expect
  end
end
