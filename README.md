Samples for CI configurations using Docker images at https://github.com/katalon-studio/docker-images.

## Companion products

### Katalon TestOps

[Katalon TestOps](https://analytics.katalon.com) is a web-based application that provides dynamic perspectives and an insightful look at your automation testing data. You can leverage your automation testing data by transforming and visualizing your data; analyzing test results; seamlessly integrating with such tools as Katalon Studio and Jira; maximizing the testing capacity with remote execution.

* Read our [documentation](https://docs.katalon.com/katalon-analytics/docs/overview.html).
* Ask a question on [Forum](https://forum.katalon.com/categories/katalon-analytics).
* Request a new feature on [GitHub](CONTRIBUTING.md).
* Vote for [Popular Feature Requests](https://github.com/katalon-analytics/katalon-analytics/issues?q=is%3Aopen+is%3Aissue+label%3Afeature-request+sort%3Areactions-%2B1-desc).
* File a bug in [GitHub Issues](https://github.com/katalon-analytics/katalon-analytics/issues).

### Katalon Studio

Katalon Studio is an all-in-one test automation solution for Web, API, mobile, and desktop built for both beginners and expert testers. [Learn more](https://docs.katalon.com/katalon-studio/docs/overview.html).

Katalon Runtime Engine (KRE) is the test execution add-on of Katalon Studio. KRE allows you to execute automation tests in CLI mode. It can be used in a variety of scenarios, such as scheduling your tests, integrating with CI/CD system, or bundling your tests to execute in virtual containers like Docker. [Learn more](https://docs.katalon.com/katalon-studio/docs/intro-RE.html)

## Supported CIs

- [x] Azure Devops Services
- [ ] AppVeyor
- [x] [AWS CodeBuild](https://docs.katalon.com/katalon-studio/docs/aws-codebuild-docker-image-integration.html)
- [x] Bitbucket
- [x] BuildKite
- [x] Circle CI
- [x] CLI
- [x] CodeShip
- [ ] Concourse
- [x] GitLab
- [x] [Google Cloud Platform Cloud Build](https://docs.katalon.com/docs/katalon-runtime-engine/cicd-tools-integration/google-cloud-build)
- [x] [Harness CI](https://github.com/HieuBui419/ci-samples/tree/harness/.harness)
- [ ] Heroku CI
- [x] Jenkins
- [ ] Semaphore
- [ ] Shippable
- [ ] Solano
- [x] TravisCI
- [ ] VSTS CI


## CLI

    cd <project_root_directory>
    ./katalon/run_chrome.sh
    ./katalon/run_firefox.sh
    
If you are running the sample in Windows, also see issue #2.
