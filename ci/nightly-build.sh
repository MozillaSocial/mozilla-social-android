#!/usr/bin/env bash

# Fail if any commands fails.
set -e

# TODO: re-add if we do nightly builds on google play
#BUNDLE_OUTPUT_DIR="app/build/outputs/bundle/nightly"
#UNSIGNED_AAB="$BUNDLE_OUTPUT_DIR/app-nightly.aab"
#SIGNED_AAB="secrets/firefly-nightly.aab"

APK_OUTPUT_DIR="app/build/outputs/apk/nightly"
UNSIGNED_APK="$APK_OUTPUT_DIR/app-nightly-unsigned.apk"
ALIGNED_APK="$APK_OUTPUT_DIR/nightly-aligned.apk"
SIGNED_APK="$APK_OUTPUT_DIR/nightly.apk"
SIGNED_APK_FINAL_PATH="secrets/nightly.apk"

BUILD_TOOLS_DIR="$ANDROID_HOME/build-tools/33.0.1"
SECRET_ENV="secrets/secret-environment-variables.sh"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "secrets/secret-environment-variables.sh"

# TODO: re-add if we do nightly builds on google play
#echo "Building AAB…"
#./gradlew clean :app:bundleNightly
#
#mv $UNSIGNED_AAB $SIGNED_AAB

echo "Building APK"
./gradlew :app:assembleNightly

echo "Aligning APK"
"$BUILD_TOOLS_DIR/zipalign" -p 4 "$UNSIGNED_APK" "$ALIGNED_APK"

echo "Signing APK"
curl \
  -F "input=@$ALIGNED_APK" \
  -o "$SIGNED_APK" \
  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
  --fail \
  https://edge.prod.autograph.services.mozaws.net/sign

mv $SIGNED_APK $SIGNED_APK_FINAL_PATH

echo "Done!"
echo "The signed APK is at $SIGNED_APK_FINAL_PATH"
