#!/usr/bin/env bash

# Fail if any commands fails.
set -e

echo "Installing rbenv…"
brew install rbenv
RBENV_SHIMS_DIR=$HOME/.rbenv/shims
# Make it work in this action.
PATH="$RBENV_SHIMS_DIR:$PATH"
# Make it work for subsequent actions.
# https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions#adding-a-system-path
echo "$RBENV_SHIMS_DIR" >>"$GITHUB_PATH"

echo "Installing ruby…"
brew install gmp
rbenv install "$(cat .ruby-version)" --verbose

echo "Installing gems…"
gem install bundler
bundle install
