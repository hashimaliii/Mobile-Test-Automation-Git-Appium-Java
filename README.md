# 📱 Mobile Test Automation Framework

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) 
![Appium](https://img.shields.io/badge/Appium-46BAEA?style=for-the-badge&logo=appium&logoColor=white) 
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

## 📖 Project Overview
This project is an enterprise-grade mobile test automation framework designed for functional testing of Android applications. Built using **Java, Appium, TestNG, and Maven**, the framework rigidly follows the **Page Object Model (POM)** design pattern to ensure clean, maintainable, and highly reusable test code.

This repository fulfills all core specifications and bonus requirements for **DevOps Assignment 1**, featuring advanced CI/CD pipelines, containerization, and interactive HTML reporting!

---

## 🛠️ Tools & Technologies
- **Language**: Java 17
- **Build Tool**: Apache Maven
- **Automation Engine**: Appium (v8.x Client, v2.x Server, UiAutomator2)
- **Test Runner**: TestNG
- **Reporting**: ExtentReports (v5.x)
- **CI/CD**: GitHub Actions (Matrix test execution)
- **Containerization**: Docker & Docker Compose
- **Version Control**: Git / GitHub

---

## 🚀 Setup Instructions

### 1️⃣ System Prerequisites
Please ensure the following tools are installed on your local machine:
- **Java Development Kit (JDK) 17**: Ensure `JAVA_HOME` is set.
- **Maven**: Ensure `mvn` is accessible via your system terminal PATH.
- **Node.js**: Required to run the local Appium server.
- **Android Studio / SDK**: You must have the Android SDK installed, along with at least one virtual device (Emulator) configured. Ensure `ANDROID_HOME` and `ANDROID_SDK_ROOT` environment variables are properly mapped.

### 2️⃣ Local Project Setup
1. **Clone the repository:**
   ```bash
   git clone https://github.com/hashimaliii/Mobile-Test-Automation-Git-Appium-Java.git
   cd Mobile-Test-Automation-Git-Appium-Java
   ```

2. **Install Appium Server globally via npm:**
   ```bash
   npm install -g appium
   ```

3. **Install the UiAutomator2 Android Driver:**
   ```bash
   appium driver install uiautomator2
   ```

4. **Boot up your Android Emulator** via Android Studio AVD Manager, or connect a physical Android device via USB (ensure USB Debugging is enabled in Developer Options). Verify the connection by running:
   ```bash
   adb devices
   ```

5. **Start the Appium Server instance** in a dedicated terminal window:
   ```bash
   appium
   ```

---

## 💻 How to Run Tests Locally

The framework is configured to run effortlessly via Maven commands.

### ▶️ Run all POM tests sequentially
```bash
mvn clean test
```
*This command executes all 10+ functional UI tests using the sequential Appium driver initialized in `testng.xml`.*

### 📊 Viewing the Test Report
Upon a successful (or failed) test execution, the framework utilizes our custom `ExtentReportListener` to dynamically generate a beautiful, interactive HTML report! 

To view it, simply open the following file in any web browser:
👉 `target/ExtentReport.html`

---

## 🐳 Docker Execution (Bonus Feature)

No local setup? No problem! The entire Appium environment and Android dependencies are fully containerized using Docker. 

You can instantly deploy the Appium instance alongside the required Java dependencies by executing:
```bash
docker-compose up --build
```
This single command spins up the Appium network, linking it to test execution layers isolated from your host machine!

*(Note: Hardware virtualization for cloud emulators requires kvm access).*

---

## ☁️ Continuous Integration (CI/CD) Workflow

We leverage an advanced **GitHub Actions** CI pipeline located at `.github/workflows/main.yml`.

### How it Works:
1. **Triggers:** The automated test pipeline fires strictly on every `push` or `pull_request` targeting the `main` branch.
2. **Matrix Execution:** It utilizes a concurrent build matrix strategy (`api-level: [28, 29]`) to simultaneously spin up massive, containerized Ubuntu runners running diverse Android Emulators natively in the cloud.
3. **Appium Initialization:** The CI script automatically installs Node, Appium, and the UiAutomator2 driver before executing `mvn clean test`.
4. **Artifact Upload:** After execution, the `actions/upload-artifact` step natively extracts and attaches the `ExtentReport.html` directly to the GitHub Dashboard, allowing engineers to download and view test results without checking out code!

---

## 🌳 Git Branching & Workflow strategy
To maintain stable, enterprise-level code quality, we strictly abide by the following Git methodologies:
- **Feature Isolation:** All development must take place on dedicated feature branches (e.g. `feature/devops-assignment-1`).
- **Protected Main Branch:** Direct commits to the `main` branch are rigidly restricted.
- **Pull Requests (PR):** Work is solely integrated via Pull Requests. Each PR must dynamically pass the GitHub Actions CI pipeline checks before it is legally eligible for a merge.
- **Code Reviews:** Peer approvals are strongly encouraged before PR execution.
