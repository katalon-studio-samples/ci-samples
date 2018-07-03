@echo on

set current_dir=%~dp0
set project_dir="%current_dir%"
set reports_dir="%project_dir%\..\reports\chrome"

rem rm -rfv "$reports_dir"
mkdir -p "%reports_dir%"

rem katalon_opts='-browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
rem docker run --rm -v "$project_dir":/katalon/katalon/source:ro -v "$reports_dir":/katalon/katalon/report -e KATALON_OPTS="$katalon_opts" katalonstudio/katalon

rem cd $current_dir
