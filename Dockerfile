FROM katalonstudio/katalon
VOLUME [ ".:/katalon/katalon/source" ]
ENTRYPOINT [ "katalon-execute.sh" ]
CMD [ "-browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest" ]