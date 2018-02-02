#!/usr/bin/env bash

rm -rfv ./reports/chrome
mkdir -p ./reports/chrome

katalon_opts='-browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
docker run --rm -v $(pwd)/katalon:/katalon/katalon/source:ro -v $(pwd)/reports/chrome:/katalon/katalon/report -e KATALON_OPTS="$katalon_opts" katalonstudio/katalon