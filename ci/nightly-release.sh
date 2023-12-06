#!/usr/bin/env bash

# Fail if any commands fails.
set -e

RELEASE_VERSION_CODE=$1

SECRET_ENV="secrets/secret-environment-variables.sh"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "$SECRET_ENV"

echo "Bumping version code to ${RELEASE_VERSION_CODE}…"
ci/set-version-code.sh "$RELEASE_VERSION_CODE"

echo "Releasing with fastlane…"
bundle exec fastlane nightly
