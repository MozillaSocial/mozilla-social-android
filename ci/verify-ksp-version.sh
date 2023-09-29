#!/usr/bin/env bash

# Fail if any commands fails.
set -e

# usage: findVersion <VERSION_NAME>
# Looks for the given version in Gradle Version Catalog (libs.versions.toml file).
findVersion() {
  local NAME=$1
  local VERSION=$(sed -nr "s/$NAME = \"([^\"]+)\"/\1/p" gradle/libs.versions.toml)
  if [[ -z "$VERSION" ]]; then
    echo "$NAME version not found"
    exit 1
  fi

  RETURN="$VERSION"
}

findVersion "kotlinKsp"
KSP_VERSION=$RETURN
findVersion "kotlin"
KOTLIN_VERSION=$RETURN

# KSP version has to start with currently used Kotlin version.
if [[ "$KSP_VERSION" =~ ^"$KOTLIN_VERSION" ]]; then
  echo "KSP version matches Kotlin version"
else
  echo "KSP version ($KSP_VERSION) doesn't match Kotlin version ($KOTLIN_VERSION)"
  exit 1
fi
