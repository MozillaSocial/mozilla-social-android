#!/usr/bin/env bash

# Fail if any commands fails.
set -e

./gradlew detekt --continue
