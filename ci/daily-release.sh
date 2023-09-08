#!/usr/bin/env bash

# Fail if any commands fails.
set -e

SECRET_ENV="secrets/secret-environment-variables.sh"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "$SECRET_ENV"

echo "Releasing with fastlane…"
bundle exec fastlane daily
