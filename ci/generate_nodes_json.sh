#!/bin/bash

# This script takes a json file that is a list of servers (as strings) and checks the monthly
# active user count.  It then outputs a new json file with the information

# get the original nodes.json file from https://nodes.fediverse.party/

if [ -z "$1" ]; then
  echo "provide the nodes.json file as an argument"
  exit 1
fi

servers=$(jq -r '.[]' "$1")
output_file="server_results.json"
max_requests=20
temp_file="/tmp/servers.tmp"

# clear the tmp file
> $temp_file

fetch_server_data() {
  local server="$1"
  echo "making request to $server"
  local response=$(curl -m 10 -sSLX GET "https://${server}/api/v2/instance")

  # check for error
  if [ $? -ne 0 ]; then
    echo "Error: Failed to get response from $server"
    return 1
  fi

  local mau=$(echo "$response" | jq -r '.usage.users.active_month')

  if [ -z "$mau" ]; then
    echo "Error: Failed to parse JSON." >&2
    exit 1
  fi

  local server_object="{\"server\": \"$server\", \"mau\": $mau}"
  echo "$server_object" >> $temp_file
}

# async get server info and write to file
for server in $servers; do
  while [[ $(jobs -r | wc -l) -ge $max_requests ]]; do
    wait -n
  done

  fetch_server_data "$server" &
done

# wait for all requests to finish
wait

servers_info=()

while IFS= read -r line; do
  servers_info+=("$line")
done < $temp_file

json_string=$(printf "%s\n" "${servers_info[@]}")
final_json=$(echo "$json_string" | jq -s '.')

echo "$final_json" > "$output_file"

echo "Results written to: $output_file"