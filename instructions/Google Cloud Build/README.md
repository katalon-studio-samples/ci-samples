<p align="center">
  <a href="" rel="noopener">
 <img width=auto height=200px src="https://avatars.githubusercontent.com/u/28861843?s=200&v=4" alt="Katalon Google Cloud Build"></a>
</p>

<h1 align="center">Katalon Google Cloud Build</h1>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/kylelobo/The-Documentation-Compendium.svg)](https://github.com/kylelobo/The-Documentation-Compendium/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/kylelobo/The-Documentation-Compendium.svg)](https://github.com/kylelobo/The-Documentation-Compendium/pulls)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center"> A sample project of using Katalon Docker image with Google Cloud Build
    <br> 
</p>

## 📝 Table of Contents

- [Getting Started](#getting_started)
- [Running the tests](#tests)
- [Reporting](#reporting)
- [References](#references)

## 🏁 Getting Started <a name = "getting_started"></a>

### Prerequisites

* A valid Katalon API Key<br/>
  Refer to [Generate API keys][Generate_API_Key] for more information about API key generation.

* A [Google Cloud Project][Create_Google_Cloud_Project]

* [Google Cloud Build API][Enable_Cloud_Build_API] must be enabled for your Google Cloud project

* [Secret Manager API][Using_Secret_Manager] must be enabled for your Google Cloud project

* [Prepare a secret][Using_Secret_Manager] to store the Katalon API key

![image](https://user-images.githubusercontent.com/46991507/163971669-d94176c6-ece5-489d-b38b-2eb9e1bad36f.png)

### Setup a cloud build project

* Create a `cloudbuild.yaml` file at the root of your katalon project

```yaml
steps:
- name: 'docker'
  args: ['pull', 'katalonstudio/katalon']
- name: 'docker'
  entrypoint: 'sh'
  args: ['-c', 'docker run -t --rm -v /workspace:/tmp/project katalonstudio/katalon katalonc.sh -projectPath=/tmp/project -browserType="Chrome" -retry=0 -retryStrategy=immediately -testSuitePath="Test Suites/TS_RegressionTest" --config -webui.autoUpdateDrivers=true -apiKey=$$KATALON_API_KEY']
  secretEnv: ['KATALON_API_KEY']
availableSecrets:
  secretManager:
  - versionName: projects/$PROJECT_ID/secrets/KATALON_API_KEY/versions/1
    env: 'KATALON_API_KEY'
```

* You may also like to enable [Katalon TestOps Integration][Katalon_Testops] from your `Project settings` so you can view the reports after the execution.

![image](https://user-images.githubusercontent.com/46991507/163970914-46bb4185-59c3-4fbb-b0bb-ad0b6c2f12ae.png)

## 🚀 Running the tests <a name = "tests"></a>

Now to run the test, you only need to create a trigger on the cloud build and run it manually or automatically 

### Connect to repo & Create a trigger

You can refer to the following document to create a build trigger: [Creating a build trigger][Create_A_Trigger]

![image](https://user-images.githubusercontent.com/46991507/163971267-969820fa-f077-4fad-8c3f-96b3f364c801.png)

### Run a build trigger

Now you can run the newly created trigger manually from [Trigger Manager][Trigger_Manager] page, or
base on the type of the trigger, it can also be triggered automatically.

![image](https://user-images.githubusercontent.com/46991507/163968161-67b440d6-a5a5-4cbd-b1c7-87bdc50af7ff.png)

### Watching the build

After triggering the build, you can go to [History tab][Cloud_Build_History] on the Google Cloud Build and watch the progress.

![image](https://user-images.githubusercontent.com/46991507/163977614-ec76b2eb-6f5c-479c-bdef-e1d7deb3e0b9.png)

![image](https://user-images.githubusercontent.com/46991507/163978194-1f876a3c-0b2b-416c-9ae3-5eda8c17cb9a.png)

## 📋 Reporting <a name="reporting"></a>

To view the reports from the build, you can enable [Katalon TestOps Integration][Katalon_Testops] on your project. After that, every report will be uploaded automatically to the TestOps server and you can go there to see the testing results.

![image](https://user-images.githubusercontent.com/46991507/163969648-cd08e919-7e52-4015-a53c-79e945a08a96.png)

## 🧐 References <a name = "references"></a>

* [Cloud Build Dashboard](https://console.cloud.google.com/cloud-build/dashboard)
* [Build configuration file schema](https://cloud.google.com/build/docs/build-config-file-schema)
* [Secret Manager][Secret_Manager]
* [Using secrets from Secret Manager][Using_Secret_Manager]
* [Cloud Builders](https://cloud.google.com/build/docs/cloud-builders)
* [Substituting variable values](https://cloud.google.com/build/docs/configuring-builds/substitute-variable-values)
* [Katalon TestOps][Katalon_Testops]
* [Trigger Manager][Trigger_Manager]

[Create_Google_Cloud_Project]: https://developers.google.com/workspace/guides/create-project
[Generate_API_Key]: https://docs.katalon.com/katalon-analytics/docs/ka-api-key.html#generate-a-katalon-api-key
[Secret_Manager]: https://console.cloud.google.com/security/secret-manager
[Using_Secret_Manager]: https://cloud.google.com/build/docs/securing-builds/use-secrets
[Enable_Cloud_Build_API]: https://cloud.google.com/build?hl=en
[Trigger_Manager]: https://console.cloud.google.com/cloud-build/triggers
[Create_A_Trigger]: https://cloud.google.com/build/docs/automating-builds/create-manage-triggers#build_trigger
[Katalon_Testops]: https://docs.katalon.com/katalon-studio/docs/testops-integration.html
[Cloud_Build_History]: https://console.cloud.google.com/cloud-build/builds