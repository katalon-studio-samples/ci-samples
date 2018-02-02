#!/usr/bin/env bash

rm -rfv /home/ubuntu/report/firefox
mkdir -p /home/ubuntu/report/firefox

katalon_opts='-browserType="Firefox" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
docker run --rm -v /home/ubuntu/katalon-test:/katalon/katalon/source:ro -v /home/ubuntu/report/firefox:/katalon/katalon/report -e KATALON_OPTS="$katalon_opts" katalon-studio/katalon