#!/usr/bin/env bash

# Fail if any commands fails.
set -e

BUILD_OUTPUT_DIR="app/build/outputs/bundle/nightly"

UNSIGNED_AAB="$BUILD_OUTPUT_DIR/app-nightly.aab"
SIGNED_AAB="secrets/mozilla-social-nightly.aab"

UNSIGNED_APK="$BUILD_OUTPUT_DIR/app-nightly.apk"
SIGNED_APK="secrets/mozilla-social-nightly.apk"

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

echo "Signing AAB…"
curl \
  -F "input=@$UNSIGNED_AAB" \
  -o "$SIGNED_AAB" \
  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
  --fail \
  https://edge.prod.autograph.services.mozaws.net/sign

echo "Signing APK…"
curl \
  -F "input=@$UNSIGNED_APK" \
  -o "$SIGNED_APK" \
  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
  --fail \
  https://edge.prod.autograph.services.mozaws.net/sign

echo "Done!"
echo "The signed AAB is at $SIGNED_AAB"
echo "The signed APK is at $SIGNED_APK"
