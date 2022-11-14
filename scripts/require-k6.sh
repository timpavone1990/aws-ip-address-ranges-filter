#!/usr/bin/env bash

set -e

if [[ $(command -v ./k6) ]]
then
  echo "k6 found in current directory" >&2
  K6=./k6
elif [[ $(command -v k6) ]]
then
  echo "k6 found" >&2
  K6=k6
else
  echo "k6 not found" >&2
  echo "Downloading k6 from GitHub" >&2
  K6_TARBALL=k6-v0.41.0-linux-amd64.tar.gz
  wget -q https://github.com/grafana/k6/releases/download/v0.41.0/$K6_TARBALL
  tar -xzf $K6_TARBALL
  mv k6-v0.41.0-linux-amd64/* .
  rm -rf k6-v0.41.0-linux-amd64
  rm $K6_TARBALL
  K6=./k6
fi

echo $K6
