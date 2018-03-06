#!/usr/bin/env bash

set -xe

current_dir=$(pwd)
project_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
reports_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )/reports/chrome"

rm -rfv "$reports_dir"
mkdir -p "$reports_dir"

katalon_opts='-browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
docker run --rm -v "$project_dir":/katalon/katalon/source:ro -v "$reports_dir":/katalon/katalon/report -e KATALON_OPTS="$katalon_opts" katalonstudio/katalon

cd $current_dir