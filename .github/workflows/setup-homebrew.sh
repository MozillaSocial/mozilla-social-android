#!/usr/bin/env bash

# Fail if any commands fails.
set -e

echo "Enabling Homebrew for subsequent actions…"
# https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions#adding-a-system-path
# https://github.com/Homebrew/actions/blob/31a4246c0ceaf73ce5b7706444a117c77a295b40/setup-homebrew/main.sh
HOMEBREW_PREFIX="/home/linuxbrew/.linuxbrew"
echo "$HOMEBREW_PREFIX/sbin" >>"$GITHUB_PATH"
echo "$HOMEBREW_PREFIX/bin" >>"$GITHUB_PATH"
