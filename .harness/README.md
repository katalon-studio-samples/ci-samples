<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="https://avatars.githubusercontent.com/u/28861843?s=200&v=4" alt="Katalon Harness CI"></a>
</p>

<h3 align="center">Katalon Harness CI</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center"> A sample project of using Katalon Docker image with Harness CI
    <br> 
</p>

## 📝 Table of Contents

- [Getting Started](#getting_started)
- [Setup](#setup)
- [Running](#running)
- [Reporting](#reporting)
- [References](#references)
- [Troubleshoot](#troubleshoot)

## 🏁 Getting Started <a name = "getting_started"></a>

### Prerequisites
- Docker

- Harness CI First Gen and Kubernetes with Docker

- A free Harness CI account

Kubernetes:
- For Mac: https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/ 
- For Windows: https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/ ,
- For Docker Desktop: https://birthday.play-with-docker.com/kubernetes-docker-desktop/

## 🔧 Setup <a name = "setup"></a>

### Install Delegate
![173531304-56827862-61d5-4ed4-83d7-7b56aa0d766a](https://user-images.githubusercontent.com/79688900/174525620-a356a57f-14bd-4ed0-901f-dbe9d16093fc.png)

From Setup on the right sidebar → choose Harness Delegate → click Install Delegate and choose the corresponding delegate

- Download Type: Who use Kubernetes with Docker will choose Docker Image, who won’t use with Docker will choose Kubernetes YAML

- Input the Name, Profile (default Primary) and token (default)

Ref: https://docs.harness.io/article/h9tkwmkrm7-delegate-installation 

Download the delegate and run it as instructed in the downloaded folder.

### Install Connector
From Setup → Connectors
![173531473-8d106799-ea24-43fa-8603-1767b362b521](https://user-images.githubusercontent.com/79688900/174525633-608b75b3-e725-4231-88dd-f7bfbb6e28a1.png)
- Choose Artifact Servers → Select Docker Registry Type → Input the Display Name and Docker Registry URL → Test the connection → Submit

- Back out and choose Source Repo Providers (this is where GitHub link) → Create a new Git connector → Test → Submit

### Install Cloud Providers
From Setup → Cloud Providers (Same column as Connector)

- Add a new Cloud Providers → Choose Kubernetes Cluster

- Input Display Name, Cluster Detail choose Enter manually

- Input Master Url (use kubectl cluster-info in cmd for URL)

- Input Authentication:

  - For Service Account Token: 

    - Run this command to get all service accounts in kubernetes namespace:

      `kubectl get serviceaccounts <namespace> -o yaml`

    - Remember the name from the secrets field above and enter this command:

      `kubectl get secret <secret name> -o yaml`

    - It will print out the ca.crt and token base64 secrets. Input the ca.crt secret into the Certificate field and decoded token into SAT Token. (You can decode base64 by using website)
    ![image](https://user-images.githubusercontent.com/79688900/173532451-b5a25f58-44be-4e54-97d4-f9fc1d6e76cb.png)

  - For other types: please see this https://docs.harness.io/article/l68rujg6mp-add-kubernetes-cluster-cloud-provider#username_and_password

### Setup Application and Workflows:
From Setup → Add a new application
![image](https://user-images.githubusercontent.com/79688900/173532640-449dd7ca-bca1-4e8d-9286-3cf967312251.png)
#### Service
- In the application, add a new service with Deployment Type is Kubernetes → It will create a new service with Artifact Type is Docker Image

  - In service: Add an artifact source as a Docker Registry Type we have defined earlier and input Docker Image Name = katalonstudio/katalon

  - Artifact History to the right: click it and then manually pull the artifact to check whether the image is right or not
  ![173532784-b2ab60cd-af25-4eec-94ee-571d13a54b64](https://user-images.githubusercontent.com/79688900/174525713-6a601754-ea9a-477a-ab71-acee6c6b2395.png)
  - Manifests (Important): You can edit the manifest right in it or link the manifest from the Github repo
  ![image](https://user-images.githubusercontent.com/79688900/173533128-ec8cd804-a2fd-46a9-8826-35b02710c19a.png)
    Manifests will define a job for kubernetes to run a docker image command. Sample YAML file can be found here: https://github.com/katalon-studio-samples/ci-samples (.harness folder)

    Ref: https://docs.harness.io/article/qpv2jfdjgm-run-kubernetes-jobs#
  - Configuration: In Config Variables: create a new apiKey variable to store your Katalon APIKey.
#### Environment
- Create a new environment, in the environment: add a new infrastructure definition
  - Cloud Provider Type: Kubernetes Cluster,
  - Deployment Type: Kubernetes
  - Choose Use Already Provisioned Infrastructure
  - Cloud Provider: input the Kubernetes cloud provider earlier
  - Namespace: existed namespace in Kubernetes cluster
#### Workflow
- Add a new workflow with type Rolling Deployment
- Environment, Service and Infrastructure Definition are inputted with those we setup earlier
![image](https://user-images.githubusercontent.com/79688900/173535769-76a2a2e5-fadd-4c83-87f4-7138276cc478.png)
- In workflow variable: create a new variable name JobName with default value is the name of your manifest file (in values.yaml or in main yaml file)
![173543405-6edbb6e9-6b9b-4bf7-bd13-b0ecb05b0ffc](https://user-images.githubusercontent.com/79688900/174525781-c2b071f6-4802-4575-9a41-65d5851d9c6b.png)
![image](https://user-images.githubusercontent.com/79688900/174270701-3c685dfe-d94a-4947-9b90-cd834f1f819b.png)

- In rolling -> Deploy: Create a new Apply step
  - File Paths: is your manifest file path (in my example is templates/test.yaml)
![image](https://user-images.githubusercontent.com/79688900/173543077-48dafd95-c9aa-4732-a340-6eb8779c8dbf.png)
- In rolling -> Wrap Up: Create a new Delete step
  - Resources: Input Job/{workflow variable value}
- In rollback also add the same Delete step
Final workflow will be like this: ![image](https://user-images.githubusercontent.com/79688900/173544931-69c3295a-b263-4651-87aa-c3bf84c8e10b.png)
## 🚀 Running <a name="running"></a>
Press the Deploy button to start the running
![173545527-f3cbbe0f-488d-4cf5-b4c9-742f7ad033a7](https://user-images.githubusercontent.com/79688900/174525816-ad6fa4d9-966f-4dff-957d-7e2cbdec62bb.png)
You can add triggers if you want to trigger the workflow when the artifact is release new, on scheduled time, on github events,...

During the execution, you can see the log in Docker container: 
![image](https://user-images.githubusercontent.com/79688900/173547210-dbbc3492-83cd-4f34-ba71-a9eabd9bd57b.png)

## 📋 Reporting <a name="reporting"></a>

To view the reports from the build, you can enable [Katalon TestOps Integration][Katalon_Testops] on your project. After that, every report will be uploaded automatically to the TestOps server and you can go there to see the testing results.

![image](https://user-images.githubusercontent.com/46991507/163969648-cd08e919-7e52-4015-a53c-79e945a08a96.png)

## ✍️ References <a name = "references"></a>

- [Harness CI Doc](https://docs.harness.io/)

- [Kubernetes Doc](https://kubernetes.io/docs/home/)

## 🔧  Troubleshoot <a name = "acknowledgement"></a>

If you don’t see the Delegate Profile/ Token, please create one here:
![173548746-3b5d4ed1-677e-4bed-94f1-1b885c77f8d8](https://user-images.githubusercontent.com/79688900/174525836-ed50cf62-d936-40e4-95dd-68010e8e65ab.png)
If you encounter **'The Job "..." is invalid: spec.template: Invalid value: core.PodTemplateSpec'**:
- Run `kubectl delete jobs --all` in cmd (due to Kubernetes job is not deleted after run)
If you encounter **User "system:anonymous" cannot get path "/".** or sth alike
- Run `kubectl create clusterrolebinding cluster-system-default --clusterrole=cluster-admin --user=<userinfo>` with userinfo is user in error (ex: system:anonymous, system:serviceaccount:default:default)

Ref: https://kubernetes.io/docs/reference/access-authn-authz/rbac/#kubectl-create-clusterrolebinding
