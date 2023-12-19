#!/usr/bin/env bash

# Fail if any commands fails.
set -e

NEW_VERSION_CODE=$1

sed -i'.bkp' -e "s/versionCode = .*/versionCode = $NEW_VERSION_CODE/" "app/build.gradle.kts"
# sed is a "stream editor", used here to edit a file
#  -i modifies the file in place (writes the edited version to the same file)
#  '.bkp' after -i specifies an extension to use for the backup file
#         we don't need a backup file, but on macos it is required, so add it for portability
#  -e specifies the command to run
#     our command finds the version code in the gradle script and updates to a new value
#  last argument is the path to the file to edit
