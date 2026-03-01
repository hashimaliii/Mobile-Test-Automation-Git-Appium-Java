# Mobile Test Automation Framework

## Project Overview
This project is a robust mobile test automation framework designed to test Android applications. It leverages a structured Page Object Model (POM) architecture to automate functional workflows on a mobile application, verifying various capabilities such as login, app launch, navigation, product browsing, and cart functionality. 

This project fulfills all core specifications and bonus criteria for the DevOps Assignment 1.

## Tools and Technologies Used
* **Java 17**: Core programming language.
* **Maven**: Build automation and dependency management.
* **Appium (v8.x Client / v2.x Server)**: Mobile application automation engine.
* **TestNG**: Testing framework for orchestrating test execution.
* **ExtentReports (v5.x)**: Generating rich, interactive HTML test reports.
* **Docker & Docker Compose**: Containerizing the Appium server ecosystem.
* **GitHub Actions**: Continuous Integration pipeline for automated testing on cloud emulators.

## Setup Instructions

### Prerequisites
1. Install **Java Development Kit (JDK) 17**.
2. Install **Maven** and configure it to your system PATH.
3. Install **Node.js** and **npm**.
4. Install **Android Studio** (or just the Command Line Tools) and configure `ANDROID_HOME` and `ANDROID_SDK_ROOT` environment variables.

### Local Setup
1. Clone the repository: `git clone <repository-url>`
2. Install the Appium server globally: `npm install -g appium`
3. Install the UiAutomator2 driver: `appium driver install uiautomator2`
4. Start your Android Emulator or connect a physical physical device via ADB (`adb devices`).
5. Start the Appium server in a new terminal: `appium`

## How to Run Tests Locally

Execute the tests using Maven from the root directory.

### Run tests sequentially
```bash
mvn clean test
```

### Run tests in parallel
Parallel execution is supported natively and configured in our system. You can easily trigger it locally:
```bash
mvn clean test -Dparallel.count=3
```

A beautiful interactive test report will be generated at `target/ExtentReport.html`!

## CI Workflow Explanation
The Continuous Integration pipeline is managed via GitHub Actions (`.github/workflows/main.yml`). 
1. **Triggers:** The pipeline runs automatically on every `push` or `pull_request` to the `main` branch.
2. **Environment:** It spins up a managed Ubuntu runner, installs Java 17, Node.js, and Appium.
3. **Emulator Matrix:** It smartly utilizes a GitHub Actions strategy matrix (`api-level: [28, 29]`) to concurrently spawn multiple Android Emulators and execute tests across different API levels in parallel.
4. **Execution:** It runs `mvn clean test` on each matrix job.
5. **Artifacts:** Finally, it utilizes the `actions/upload-artifact` action to scrape the `ExtentReport.html` file from the target branch and upload it natively to the GitHub Actions run summary for easy viewing.

## Git Workflow Used
* **Feature Branches**: All development happens in isolated feature branches (e.g., `feature/devops-assignment-1`).
* **Pull Requests (PR)**: Code is merged into `main` exclusively through Pull Requests. Direct commits to `main` are disabled via branch protection rules.
* **Code Reviews**: All PRs require at least one approval before merging.
* **Issue Tracking**: Features and assignments are scoped into GitHub Issues and assigned evenly amongst the group.

## Dockerized Appium Setup (Bonus)
The project ships with a containerized Appium test environment! 
You can spin up the environment with zero local configuration by simply running:

```bash
docker-compose up --build
```
This spawns an Appium server container automatically linked to your local testing scope!
