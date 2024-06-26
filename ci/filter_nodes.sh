#!/bin/bash

# this script filters the json generated by generate_nodes_json.sh

if [ -z "$1" ]; then
  echo "provide the json file output from generate_nodes_json.sh as an argument"
  exit 1
fi

input_file=$1
output_file="nodes.json"

filtered_json=$(jq '[.[] | select(.mau >= 10)]' "$input_file")

echo "$filtered_json" > "$output_file"

echo "Filtered entries have been written to $output_file."
