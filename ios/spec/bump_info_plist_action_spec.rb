require "spec_helper"
require "tempfile"

describe Fastlane::Actions::BumpInfoPlistAction do
  let(:sample_plist) do
    <<~PLIST
      <?xml version="1.0" encoding="UTF-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
      <plist version="1.0">
      <dict>
        <key>CFBundleShortVersionString</key>
        <string>0.0.0</string>
        <key>CFBundleVersion</key>
        <string>0</string>
      </dict>
      </plist>
    PLIST
  end

  def with_plist
    f = Tempfile.new(["Info", ".plist"])
    f.write(sample_plist)
    f.close
    yield f.path
  ensure
    File.unlink(f.path) if f && File.exist?(f.path)
  end

  it "writes both CFBundleShortVersionString and CFBundleVersion when semver is provided" do
    with_plist do |path|
      described_class.run(plist: path, semver: "1.2.3")
      content = File.read(path)
      expect(content).to include("<string>1.2.3</string>")
      expect(content).to match(%r{<key>CFBundleVersion</key>\s*<string>\d+</string>})
    end
  end

  it "leaves CFBundleShortVersionString untouched when semver is omitted" do
    with_plist do |path|
      described_class.run(plist: path, semver: nil)
      content = File.read(path)
      expect(content).to include("<string>0.0.0</string>")
      expect(content).to match(%r{<key>CFBundleVersion</key>\s*<string>\d+</string>})
    end
  end

  it "raises when plist path does not exist" do
    expect {
      described_class.run(plist: "/tmp/does-not-exist.plist", semver: "1.0.0")
    }.to raise_error(FastlaneCore::Interface::FastlaneError)
  end
end
