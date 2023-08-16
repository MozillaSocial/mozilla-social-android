#!/usr/bin/env bash

# Fail if any commands fails.
set -e

echo "Accepting Android SDK licenses…"
yes | "${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager" --licenses > /dev/null

echo "Decrypting secrets…"
(cd secrets && ./decrypt.sh)

echo "Building the release…"
secrets/build_daily.sh
