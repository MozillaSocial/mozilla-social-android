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
decrypt "daily.jks"
decrypt "build_daily.sh" && chmod +x build_daily.sh
