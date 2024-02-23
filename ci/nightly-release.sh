#!/usr/bin/env bash

# Fail if any commands fails.
set -e

RELEASE_VERSION_CODE=$1
GITHUB_TOKEN=$2

KEY_STORE="secrets/firefly.jks"

if [[ ! -f "$KEY_STORE" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

# TODO: re-add if we ever add secret env vars
#echo "Initializing secrets…"
#source "secrets/secret-environment-variables.sh"

echo "Bumping version code to ${RELEASE_VERSION_CODE}…"
ci/set-version-code.sh "$RELEASE_VERSION_CODE"

VERSION_NAME=$(cat app/build.gradle.kts | grep versionName | cut -d "\"" -f2)
TAG="$VERSION_NAME.$RELEASE_VERSION_CODE"
RELEASE_NAME="Nightly $TAG"

echo "Releasing with fastlane…"
bundle exec fastlane nightly token:$GITHUB_TOKEN name:"$RELEASE_NAME" tag:"$TAG"
