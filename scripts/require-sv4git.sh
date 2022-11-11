#!/usr/bin/env bash

set -e

if [[ $(command -v ./git-sv) ]]
then
  echo "git-sv found in current directory" >&2
  GIT_SV=./git-sv
elif [[ $(command -v git-sv) ]]
then
  echo "git-sv found" >&2
  GIT_SV=git-sv
else
  echo "git-sv not found" >&2
  echo "Downloading git-sv from GitHub" >&2
  GIT_SV_TARBALL=git-sv_2.8.1_linux_amd64.tar.gz
  wget -q https://github.com/bvieira/sv4git/releases/download/v2.8.1/$GIT_SV_TARBALL
  tar -xzf $GIT_SV_TARBALL
  rm $GIT_SV_TARBALL
  GIT_SV=./git-sv
fi

echo $GIT_SV
