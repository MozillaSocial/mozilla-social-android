#!/usr/bin/env bash

if [[ -z "$GPG_KEY" ]]; then
  read -p "Mozilla Social Android GPG key: " -r -s
  echo # (optional) move to a new line
  GPG_KEY="$REPLY"
fi

gpg --batch --yes --passphrase="$GPG_KEY" --cipher-algo AES256 --symmetric --output "secret.properties.gpg" "secret.properties"
