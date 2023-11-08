#!/usr/bin/env bash

# Fail if any commands fails.
set -e

BUILD_OUTPUT_DIR="app/build/outputs/apk/nightly"
UNSIGNED_APK="$BUILD_OUTPUT_DIR/app-nightly-unsigned.apk"
SIGNED_APK="secrets/mozilla-social-nightly.apk"

SECRET_ENV="secrets/secret-environment-variables.sh"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "$SECRET_ENV"

echo "Building…"
./gradlew clean :app:assembleNightly

echo "Signing…"
curl \
  -F "input=@$UNSIGNED_APK" \
  -o "$SIGNED_APK" \
  -H "Authorization: $AUTOGRAPH_EDGE_NIGHTLY_CLIENT_TOKEN" \
  --fail \
  https://edge.prod.autograph.services.mozaws.net/sign

echo "Done!"
echo "The signed APK is at $SIGNED_APK"
