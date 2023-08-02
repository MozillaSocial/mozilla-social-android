#!/usr/bin/env bash

if [[ -z "$GPG_KEY" ]]; then
  read -p "Mozilla Social Android GPG key: " -r -s
  echo # (optional) move to a new line
  GPG_KEY="$REPLY"
fi

gpg --quiet --batch --yes --decrypt --passphrase="$GPG_KEY" --output "secret.properties" "secret.properties.gpg"
