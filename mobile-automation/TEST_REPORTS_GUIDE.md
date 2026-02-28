# Test Report Generation & Parallel Execution Guide

## Overview

This guide explains how to use the enhanced Maven Surefire plugin with parallel test execution and comprehensive HTML report generation.

## 📊 Report Generation

### Generated Report Locations

After running tests, reports are available at:

```
target/surefire-reports/
├── index.html                           # Main HTML test report
├── emailable-report.html                # Email-friendly report
├── testng-results.xml                   # TestNG XML results
├── testng-report.html                   # TestNG detailed report
├── junitreports/                        # JUnit format reports
│   ├── TEST-tests.LoginTest.xml
│   ├── TEST-tests.CartTest.xml
│   ├── TEST-tests.NavigationTest.xml
│   ├── TEST-tests.ProductBrowsingTest.xml
│   ├── TEST-tests.CartFunctionalTest.xml
│   ├── TEST-tests.CheckoutFunctionalTest.xml
│   ├── TEST-tests.AccessibilityTest.xml
│   └── TEST-tests.AppLaunchTest.xml
├── testng-reports.css                  # Report styling
├── testng-reports.js                   # Report interactivity
└── TestSuite.txt                        # Summary text report
```

### View Reports

#### HTML Report (Recommended)

```bash
# Linux/Mac
open target/surefire-reports/index.html

# Windows
start target\surefire-reports\index.html
```

#### Report Contents

1. **Summary Section**
   - Total tests executed
   - Pass/Fail/Skip counts
   - Success percentage
   - Total execution time

2. **Detailed Results**
   - Test class breakdown
   - Individual test results
   - Execution duration per test
   - Error messages and stack traces

3. **Graphs & Charts**
   - Success rate visualization
   - Execution time trends
   - Test distribution

## ⚡ Parallel Test Execution

### Parallel Modes

The framework supports three parallel execution strategies:

#### 1. **Parallel by Methods** (Default - Fastest)

```xml
<suite parallel="methods" thread-count="3">
```

- Runs individual test methods across multiple threads
- Best for independent tests
- Fastest execution
- Default configuration (3 threads)

**Example:**
```
Test1.method1() - Thread 1
Test1.method2() - Thread 2
Test2.method1() - Thread 3
Test2.method2() - Thread 1 (reused)
```

#### 2. **Parallel by Classes**

```xml
<suite parallel="classes" thread-count="3">
```

- Runs entire test classes in parallel
- Ensures tests within a class run sequentially
- Useful for test dependencies

**Example:**
```
LoginTest (all methods) - Thread 1
CartTest (all methods) - Thread 2
AccessibilityTest (all methods) - Thread 3
```

#### 3. **Parallel by Tests**

```xml
<suite parallel="tests" thread-count="3">
```

- Runs each `<test>` element in parallel
- Groups related tests together
- Moderate parallelization

### Running Tests with Parallel Execution

#### Default Parallel (3 threads, methods)

```bash
mvn clean test
```

#### Custom Thread Count

```bash
# Run with 4 parallel threads
mvn clean test -Dparallel.count=4

# Run with 2 parallel threads (lighter load)
mvn clean test -Dparallel.count=2
```

#### Disable Parallel (Sequential)

```bash
# Run tests sequentially (one at a time)
mvn clean test -Dparallel.count=1
```

#### With Custom Timeout

```bash
# Set test timeout to 120 seconds (120000ms)
mvn clean test -Dtimeout=120000
```

### Performance Metrics

#### Example Execution Summary

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S  (Parallel - 3 threads)
[INFO] -------------------------------------------------------
[INFO] Running TestSuite
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO] Execution Time: 6 minutes 42 seconds (Sequential: ~21 minutes)
[INFO] Performance Gain: 70% faster with 3 parallel threads
[INFO] -------------------------------------------------------
```

## 🎯 Maven Commands Reference

### Test Execution

```bash
# Standard execution (parallel by methods, 3 threads)
mvn clean test

# Skip tests during build
mvn clean install -DskipTests

# Execute only specific test file
mvn test -Dtest=CartTest

# Execute multiple test classes
mvn test -Dtest=CartTest,LoginTest,NavigationTest

# Execute specific test method
mvn test -Dtest=CartTest#testCartButtonIsClickable

# Generate test report
mvn surefire-report:report

# View report in browser
mvn surefire-report:report site:stage -DstagingDirectory=target/site
```

### Build and Test

```bash
# Clean build with tests
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Build and generate reports
mvn clean test site
```

### Debugging

```bash
# Run with verbose output
mvn clean test -X

# Run with debugging (for IDE debugging)
mvn -Dmaven.surefire.debug test

# Show test timing
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

## 📋 TestNG Configuration

### testng.xml Structure

The `testng.xml` file configures:

```xml
<suite name="suite-name" 
       parallel="methods|classes|tests" 
       thread-count="number" 
       verbose="level"
       timeout="milliseconds">
    
    <test name="test-group-name">
        <classes>
            <class name="tests.ClassName" />
        </classes>
    </test>
</suite>
```

### Modifying Test Groups

To add or remove tests:

```xml
<!-- Add new test class -->
<test name="New Tests">
    <classes>
        <class name="tests.NewTestClass" />
    </classes>
</test>

<!-- Remove test by commenting -->
<!-- 
<test name="Disabled Tests">
    <classes>
        <class name="tests.DisabledTest" />
    </classes>
</test>
-->
```

### Verbose Levels

```xml
<!-- Verbose Level Options -->
verbose="0"  <!-- Minimal output -->
verbose="1"  <!-- Standard output -->
verbose="2"  <!-- Detailed output (recommended) -->
verbose="10" <!-- Very detailed output -->
```

## 📈 Analyzing Results

### HTML Report Sections

#### 1. Test Summary
- Total number of tests
- Passed/Failed/Skipped breakdown
- Execution duration
- Success percentage

#### 2. Test Groups (by class)
- Test class name
- Count of passed/failed tests
- Methods and individual results
- Stack traces for failures

#### 3. Graphs
- Pie chart: Pass/Fail distribution
- Bar chart: Execution time per test

### Common Report Patterns

#### All Tests Pass
```
Results: 24 passed, 0 failed, 0 skipped
Time: 6m 42s
Status: ✅ SUCCESS
```

#### Some Tests Fail
```
Results: 20 passed, 4 failed, 0 skipped
Time: 7m 15s
Status: ❌ FAILURE

Failed Tests:
- tests.CartTest.testAddToCart (TimeoutException)
- tests.LoginTest.testInvalidCredentials (AssertionError)
```

#### Tests Skipped
```
Results: 20 passed, 0 failed, 4 skipped
Status: ⚠️ WITH SKIPS

Skipped Tests:
- tests.CheckoutTest.testPayment (@Ignore)
```

## 🔍 Troubleshooting

### Tests Pass Sequentially but Fail in Parallel

**Cause:** Test interdependencies or shared state

**Solution:**
```bash
# Run with fewer threads
mvn test -Dparallel.count=1

# Or use parallel by classes
# Modify testng.xml: parallel="classes"
```

### Flaky Tests in Parallel Mode

**Cause:** Race conditions or timing issues

**Solution:**
```java
// Increase wait timeouts in BasePage
private static final int WAIT_TIMEOUT = 40; // Increase from 30

// Or disable parallelization for that test
@Test(singleThreaded = true)
public void flakeyTest() {
    // This test runs on single thread
}
```

### Report Not Generated

**Cause:** Tests may have been skipped or didn't run

**Solution:**
```bash
# Force report generation
mvn clean test surefire-report:report

# Check for test classes in correct location
# Tests should be in: src/test/java/tests/
```

### Permission Denied Opening Reports

```bash
# On Linux/Mac
chmod +x target/surefire-reports/
open target/surefire-reports/index.html

# On Windows
powershell.exe -Command "Start-Process target\surefire-reports\index.html"
```

## 🚀 CI/CD Integration

### GitHub Actions Example

```yaml
name: Mobile Tests with Reporting
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      
      - name: Run Tests with Parallel Execution
        run: mvn clean test -Dparallel.count=4
      
      - name: Generate Reports
        run: mvn surefire-report:report
        if: always()  # Generate even if tests fail
      
      - name: Upload Test Reports
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: target/surefire-reports/
        if: always()
      
      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v2
        with:
          files: target/surefire-reports/testng-results.xml
        if: always()
```

### Jenkins Configuration

```groovy
pipeline {
    stage('Test') {
        steps {
            // Run with 4 parallel threads on Jenkins
            sh 'mvn clean test -Dparallel.count=4'
        }
    }
    
    stage('Report') {
        steps {
            // Generate HTML reports
            sh 'mvn surefire-report:report'
            
            // Archive reports
            archiveArtifacts artifacts: 'target/surefire-reports/**/*', 
                             allowEmptyArchive: true
            
            // Publish TestNG results
            testng([
                reportFilePath: 'target/surefire-reports/testng-results.xml'
            ])
        }
    }
}
```

## 📊 Performance Optimization

### Thread Count Selection

| Hardware | Recommended | Max Safe |
|----------|-------------|----------|
| 2 cores | 2-3 threads | 4 threads |
| 4 cores | 3-4 threads | 6 threads |
| 8 cores | 4-6 threads | 8 threads |
| 16+ cores | 8-10 threads | 16 threads |

### Emulator Resources

When using Android emulator:
- Each emulator needs ~500MB RAM
- Parallel threads = multiple emulator instances
- Recommended: Keep `thread-count <= available_emulators`

```bash
# Calculate optimal thread count
Threads = (Available_RAM - System_RAM) / (500MB per emulator)
```

## 📚 Additional Resources

- [Maven Surefire Plugin Docs](https://maven.apache.org/surefire/)
- [TestNG Documentation](https://testng.org/)
- [TestNG XML Suite Files](https://testng.org/doc/documentation-main.html#testng-xml)
- [Parallel Execution Best Practices](https://testng.org/doc/documentation-main.html#parallel-running)

---

**Last Updated:** March 1, 2026  
**Configuration Version:** 1.0
