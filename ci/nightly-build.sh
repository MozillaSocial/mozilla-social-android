#!/usr/bin/env bash

# Fail if any commands fails.
set -e

BUNDLE_OUTPUT_DIR="app/build/outputs/bundle/nightly"
UNSIGNED_AAB="$BUNDLE_OUTPUT_DIR/app-nightly.aab"
SIGNED_AAB="secrets/firefly-nightly.aab"

APK_OUTPUT_DIR="app/build/outputs/apk/nightly"
UNSIGNED_APK="$APK_OUTPUT_DIR/app-nightly-unsigned.apk"
ALIGNED_APK="$APK_OUTPUT_DIR/app-nightly-unsigned-aligned.apk"
SIGNED_APK="secrets/firefly-nightly.apk"

BUILD_TOOLS_DIR="$ANDROID_HOME/build-tools/33.0.1"
SECRET_ENV="secrets/secret-environment-variables.sh"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "$SECRET_ENV"

echo "Building AAB…"
./gradlew clean :app:bundleNightly

echo "Building APK…"
./gradlew :app:assembleNightly

echo "Aligning APK…"
"$BUILD_TOOLS_DIR/zipalign" -p 4 "$UNSIGNED_APK" "$ALIGNED_APK"

# TODO: sign APK and AAB
#echo "Signing AAB…"
#curl \
#  -F "input=@$UNSIGNED_AAB" \
#  -o "$SIGNED_AAB" \
#  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
#  --fail \
#  https://edge.prod.autograph.services.mozaws.net/sign

#echo "Signing APK…"
#curl \
#  -F "input=@$ALIGNED_APK" \
#  -o "$SIGNED_APK" \
#  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
#  --fail \
#  https://edge.prod.autograph.services.mozaws.net/sign

echo "Done!"
echo "The signed AAB is at $SIGNED_AAB"
echo "The signed APK is at $SIGNED_APK"
