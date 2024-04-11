#!/usr/bin/env bash

# Fail if any commands fails.
set -e

# TODO: re-add if we do nightly builds on google play
#BUNDLE_OUTPUT_DIR="app/build/outputs/bundle/nightly"
#UNSIGNED_AAB="$BUNDLE_OUTPUT_DIR/app-nightly.aab"
#SIGNED_AAB="secrets/firefly-nightly.aab"

APK_OUTPUT_DIR="app/build/outputs/apk/nightly"
SIGNED_APK="$APK_OUTPUT_DIR/app-nightly.apk"
SIGNED_APK_FINAL_PATH="secrets/firefly-nightly.apk"

BUILD_TOOLS_DIR="$ANDROID_HOME/build-tools/33.0.1"
KEY_STORE="secrets/firefly.jks"

if [[ ! -f "$KEY_STORE" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

# TODO: re-add if we ever add secret env vars
#echo "Initializing secrets…"
#source "secrets/secret-environment-variables.sh"

# TODO: re-add if we do nightly builds on google play
#echo "Building AAB…"
#./gradlew clean :app:bundleNightly
#
#mv $UNSIGNED_AAB $SIGNED_AAB

echo "Building APK…"
./gradlew :app:assembleNightly

mv $SIGNED_APK $SIGNED_APK_FINAL_PATH

echo "Done!"
echo "The signed APK is at $SIGNED_APK_FINAL_PATH"
