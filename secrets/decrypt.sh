#!/usr/bin/env bash

decrypt() {
  OUTPUT=$1
  gpg --quiet --batch --yes --decrypt --passphrase="$GPG_KEY" --output "$OUTPUT" "$OUTPUT.gpg"
}

if [[ -z "$GPG_KEY" ]]; then
  read -p "Mozilla Social Android GPG key: " -r -s
  echo # (optional) move to a new line
  GPG_KEY="$REPLY"
fi

decrypt "secret.properties"
decrypt "../app/sentry.properties"
decrypt "secret-environment-variables.sh" && chmod +x secret-environment-variables.sh
decrypt "boxwood-axon-825-ed7aa5764ee6.json"
