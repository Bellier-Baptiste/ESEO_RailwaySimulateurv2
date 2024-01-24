# SonarQube installation guide

## Introduction

The purpose of this document is to help users through the installation of
SonarQube.

## Summary

- [SonarQube installation guide](#sonarqube-installation-guide)
  - [Introduction](#introduction)
  - [Summary](#summary)
  - [Installation Steps](#installation-steps)
    - [Sonar rules](#sonar-rules)
    - [Run a Sonar](#run-a-sonar)

## Get a SonarQube instance

Please note that the SonarQube server will probably no longer be available by
the time this project is taken over.

## Installation Steps

### Sonar rules

To apply the rules of [SonarLint](sonarlint_guide_QLT_PFE_.pdf) into SonarQube,
you have to ask a professor to do it.

### Run a Sonar

To run a Sonar, you have to run the following command from the root of the
project on the branch you want to analyze:

```bash
set SONAR_TOKEN=<your_token>
```

```bash
mvn verify sonar:sonar -Dsonar.projectKey=<name_of_your_sonar_project> -Dsonar.host.url="<url_of_your_sonarqube_instance>/sonar" -Dsonar.login=%SONAR_TOKEN% -Dsonar.exclusions=**/JMapViewer-master/**,**/*_test.go,**/view/**
```

