# Docker Troubleshooting & FAQ

## Troubleshooting Guide

### Issue: Port Already in Use

**Error Message:**
```
Error response from daemon: driver failed programming external connectivity on endpoint appium-test
Error starting userland proxy: error binding to 0.0.0.0:4723
```

**Solutions:**

1. **Find and kill process using port:**
```bash
# Linux/macOS
lsof -ti:4723 | xargs kill -9

# Windows (PowerShell)
Get-Process -Id (Get-NetTCPConnection -LocalPort 4723).OwningProcess | Stop-Process -Force
```

2. **Use different port:**
```bash
# Modify docker-compose.yml
ports:
  - "4724:4723"  # External port 4724 maps to internal 4723

# Then connect to: http://localhost:4724
```

3. **Check what's using the port:**
```bash
# Find process
sudo netstat -tlnp | grep 4723

# Or with lsof
sudo lsof -i :4723
```

---

### Issue: Docker Build Fails

**Error Message:**
```
ERROR: failed to solve with frontend dockerfile.v0
```

**Solutions:**

1. **Clear build cache:**
```bash
docker build --no-cache -t appium-test:latest .
```

2. **Check Dockerfile syntax:**
```bash
# Lint Dockerfile
docker run --rm -i hadolint/hadolint < Dockerfile
```

3. **Check internet connectivity:**
```bash
# During build, Docker downloads base images and dependencies
# Ensure you have stable internet connection

# Retry build
docker build -t appium-test:latest .
```

4. **Insufficient disk space:**
```bash
# Check available space
df -h

# Clean up Docker resources
docker system prune -a --volumes
```

---

### Issue: Out of Memory / Containers Killed

**Error Message:**
```
Killed container or Process got signal SIGKILL
java.lang.OutOfMemoryError
```

**Solutions:**

1. **Increase Docker memory limit:**
   - Docker Desktop Settings → Resources → Memory (increase from 4GB to 8GB+)

2. **Limit container memory:**
```yaml
# docker-compose.yml
deploy:
  resources:
    limits:
      memory: 2G
```

3. **Reduce JVM heap size:**
```bash
# Set environment variable
export JAVA_OPTS="-Xmx512m -XX:MaxPermSize=256m"

# Or in docker-compose.yml
environment:
  - JAVA_OPTS=-Xmx512m
```

4. **Monitor memory usage:**
```bash
# Real-time stats
docker stats

# Check container memory
docker inspect appium-server | grep -i memory
```

---

### Issue: Appium Server Not Responding

**Error Message:**
```
ConnectionRefusedError: Failed to establish a connection
curl: (7) Failed to connect to localhost port 4723
```

**Solutions:**

1. **Check if Appium is running:**
```bash
# Check status
curl http://localhost:4723/wd/hub/status

# Check logs
docker logs appium-server
```

2. **Wait for Appium to start:**
```bash
# Add delay in test initialization
Thread.sleep(5000);  // Wait 5 seconds

# Or check health before running tests
docker-compose exec -T appium-server curl -f http://localhost:4723/wd/hub/status
```

3. **Check port mapping:**
```bash
# Verify ports are mapped
docker-compose ps

# Should show: 0.0.0.0:4723->4723/tcp
```

4. **Check container logs for errors:**
```bash
# View all logs
docker logs appium-server

# With timestamps
docker logs -f --timestamps appium-server

# Last 100 lines
docker logs --tail 100 appium-server
```

5. **Test connectivity:**
```bash
# From host
curl -v http://localhost:4723/wd/hub/status

# From another container
docker exec appium-server curl http://localhost:4723/wd/hub/status

# Test Docker network resolution
docker exec appium-server ping appium-server
```

---

### Issue: Tests Timeout

**Error Message:**
```
StaleElementReferenceException
TimeoutException: Implicit wait timeout reached
```

**Solutions:**

1. **Increase wait timeouts:**
```java
// BaseTest.java
UiAutomator2Options options = new UiAutomator2Options()
    .setAppWaitDuration(Duration.ofSeconds(120))  // Increase from 60
    // ... other options

WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));  // Increase from 30
```

2. **Increase Maven test timeout:**
```bash
# Set timeout environment variable
export MAVEN_OPTS="-Dcucumber.execution.timeout=300000"

# Or in docker-compose.yml
environment:
  - MAVEN_OPTS=-Dcucumber.execution.timeout=300000
```

3. **Check Appium logs:**
```bash
# Enable debug logging
export APPIUM_LOG_LEVEL=debug

# Or in docker-compose.yml
environment:
  - APPIUM_LOG_LEVEL=debug
```

4. **Check device/emulator responsiveness:**
```bash
# Verify device is connected
docker exec appium-server adb devices

# Check app installation
docker exec appium-server adb shell pm list packages | grep com.saucelabs
```

---

### Issue: Test Fails to Launch App

**Error Message:**
```
SessionNotCreatedException: Could not start a new session
AppiumException: Error executing command
```

**Solutions:**

1. **Check APK file exists:**
```bash
# Verify APK is in app directory
ls -la ./app/
# Should show: mda-2.2.0-25.apk

# Check in container
docker exec appium-server ls -la /workspace/app/
```

2. **Check appPackage and appActivity:**
```java
// Verify these match the actual app
.setAppPackage("com.saucelabs.mydemoapp.android")
.setAppActivity("com.saucelabs.mydemoapp.android.view.activities.SplashActivity")
.setAppWaitActivity("com.saucelabs.mydemoapp.android.view.activities.MainActivity")
```

3. **Check device availability:**
```bash
# List connected devices
docker exec appium-server adb devices

# Should show: emulator-5554 device (or your device name)
```

4. **Check app permissions:**
```bash
# Grant runtime permissions
adb shell pm grant com.saucelabs.mydemoapp.android android.permission.CAMERA
adb shell pm grant com.saucelabs.mydemoapp.android android.permission.ACCESS_FINE_LOCATION
```

5. **Check Appium server logs:**
```bash
docker logs -f appium-server

# Look for error messages in startup
```

---

### Issue: Element Not Found / Not Visible

**Error Message:**
```
NoSuchElementException
ElementNotVisibleException
```

**Solutions:**

1. **Use explicit waits instead of implicit:**
```java
// Don't rely on implicit wait alone
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

// Wait for element to be visible
wait.until(ExpectedConditions.visibilityOfElementLocated(
    AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")));

// Or wait for clickability
wait.until(ExpectedConditions.elementToBeClickable(
    AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")));
```

2. **Add delays for animations:**
```java
// Wait for UI animations to complete
Thread.sleep(1500);  // 1.5 seconds

// Then interact
element.click();
```

3. **Get page source for debugging:**
```java
// Print current page source
String source = driver.getPageSource();
System.out.println(source);

// Or save to file
Files.write(Paths.get("page_source.xml"), source.getBytes());
```

4. **Update selectors:**
```java
// If element is not found, update selector
// Try different approaches:

// 1. By resource ID
AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")

// 2. By XPath
AppiumBy.xpath("//androidx.drawerlayout.widget.DrawerLayout")

// 3. By content description
AppiumBy.xpath("//*[@content-desc='View menu']")

// 4. By text
AppiumBy.xpath("//android.widget.TextView[@text='Products']")
```

---

### Issue: Emulator Not Starting

**Error Message:**
```
ERROR | Could not start Emulator
ERROR | Cannot start KVM

```

**Solutions:**

1. **Check KVM support (Linux only):**
```bash
# For Intel
grep -cw vmx /proc/cpuinfo
# Output > 0 means supported

# For AMD
grep -cw svm /proc/cpuinfo
# Output > 0 means supported
```

2. **Enable KVM in BIOS:**
   - Restart computer and enter BIOS
   - Find "Virtualization Technology" or "AMD-V"
   - Enable and save
   - Restart

3. **Install KVM tools:**
```bash
# Ubuntu/Debian
sudo apt-get install qemu-kvm libvirt-daemon-system

# CentOS/RHEL
sudo yum install qemu-kvm libvirt
```

4. **Check Docker can access KVM:**
```bash
# Add Docker user to kvm group
sudo usermod -a -G kvm $USER

# Restart Docker
sudo systemctl restart docker
```

5. **For macOS/Windows - Use cloud service instead:**
   - Nested virtualization not supported
   - Use Sauce Labs, BrowserStack, or other cloud services
   - Update BaseTest.java to use cloud URL

---

### Issue: Docker Compose Network Issues

**Error Message:**
```
Cannot connect to container by hostname
Name resolution failed
```

**Solutions:**

1. **Check network exists:**
```bash
# List networks
docker network ls

# Should show: appium-network

# Inspect network
docker network inspect appium-network
```

2. **Check DNS resolution:**
```bash
# Test DNS from container
docker exec appium-server nslookup appium-server

# Or ping
docker exec appium-server ping appium-server
```

3. **Verify network is connected:**
```bash
# Check container network settings
docker inspect appium-server | grep -A 10 NetworkSettings

# Should show appium-network connection
```

4. **Recreate network:**
```bash
# Remove and recreate
docker-compose down
docker network rm appium-network 2>/dev/null || true
docker-compose up
```

---

### Issue: Volume Mount Not Working

**Error Message:**
```
No such file or directory
Cannot find /workspace/src
```

**Solutions:**

1. **Check paths are correct:**
```bash
# Absolute paths required
# Wrong: ./src:/workspace/src
# Correct: /absolute/path/to/src:/workspace/src

# On Windows, use forward slashes or PowerShell
# C:/Users/User/project/src:/workspace/src
```

2. **Check file permissions:**
```bash
# Linux/macOS - ensure readable
chmod -R 755 ./src ./app

# Check ownership
ls -la src/
# Should not show permission denied
```

3. **Verify volume mounting:**
```bash
# Check what's mounted
docker inspect appium-server | grep -A 20 Mounts

# Test by listing directory
docker exec appium-server ls -la /workspace/src/
```

4. **On Windows with WSL:**
```bash
# Use WSL paths, not Windows paths
# WSL: /home/user/project
# NOT: C:\Users\user\project

# Or use absolute Windows paths with forward slashes
# C:/Users/user/project/src:/workspace/src
```

---

## FAQ

### Q: How much disk space do I need?

**A:** Approximately 10-12GB:
- Base OS image: 1GB
- Java + Maven + dependencies: 3GB
- Android SDK: 4GB
- Test artifacts and logs: 2-3GB

```bash
# Check image size
docker images appium-test:latest

# Check disk usage
docker system df
```

### Q: Can I run this on macOS/Windows with emulator?

**A:** Not recommended:
- Docker on macOS runs on Linux VM (nested virtualization)
- Docker on Windows runs on Hyper-V (also nested)
- Nested KVM doesn't work reliably

**Better options:**
- Use cloud Appium services (Sauce Labs, BrowserStack)
- Use physical Android devices over USB
- Run emulator separately on Linux, connect from Docker

### Q: How do I connect to a real Android device?

**A:** Instead of emulator:

```yaml
# docker-compose.yml
environment:
  - APPIUM_DEVICE_NAME=physical_device
  - APPIUM_DEVICE_UDID=your_device_serial

# Or in BaseTest.java
.setDeviceName("physical_device")
.setUdid("R5CT817TJVX")  # Device serial from: adb devices
```

### Q: How do I run tests in parallel?

**A:** Configure Maven Surefire:

```bash
docker-compose exec appium-server mvn test \
  -DthreadCount=3 \
  -DparallelTestClasses=true
```

Or in pom.xml:
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
    <parallel>classes</parallel>
    <threadCount>3</threadCount>
  </configuration>
</plugin>
```

### Q: How do I use my own cloud Appium service?

**A:** Modify BaseTest.java:

```java
// For Sauce Labs
URL appiumURL = new URL("https://" + 
    System.getenv("SAUCE_USERNAME") + ":" + 
    System.getenv("SAUCE_ACCESS_KEY") + 
    "@ondemand.saucelabs.com/wd/hub");

// For BrowserStack
URL appiumURL = new URL("https://" + 
    System.getenv("BS_USERNAME") + ":" + 
    System.getenv("BS_ACCESS_KEY") + 
    "@hub.browserstack.com/wd/hub");

driver = new AndroidDriver(appiumURL, options);
```

Set environment:
```bash
export SAUCE_USERNAME=your_username
export SAUCE_ACCESS_KEY=your_key

docker-compose up --build
```

### Q: How do I keep my API keys secure?

**A:** Never commit secrets:

```bash
# 1. Add to .gitignore
echo ".env" >> .gitignore

# 2. Use .env.local (not in git)
cp .env.template .env.local
# Edit .env.local with sensitive data

# 3. Load in docker-compose.yml
env_file:
  - .env.template  # Defaults
  - .env.local     # Overrides

# 4. In CI/CD, use secrets
# GitHub Actions: Settings → Secrets
# GitLab CI: Settings → CI/CD Variables
```

### Q: How do I debug failing tests?

**A:** Enable debug logging:

```bash
# Method 1: Environment variable
docker-compose -e APPIUM_LOG_LEVEL=debug up

# Method 2: docker-compose.yml
environment:
  - APPIUM_LOG_LEVEL=debug

# Method 3: Open container shell
docker-compose exec appium-server /bin/bash
cd /workspace
mvn test -X  # Maven debug mode
```

### Q: How do I view test results outside the container?

**A:** Results are in ./target/surefire-reports/ (mounted volume)

```bash
# Generate HTML report
docker-compose exec appium-server mvn surefire-report:report

# Open report
open target/surefire-reports/emailable-report.html  # macOS
xdg-open target/surefire-reports/emailable-report.html  # Linux
start target\surefire-reports\emailable-report.html  # Windows
```

### Q: How do I use custom Appium capabilities?

**A:** Modify BaseTest.java:

```java
UiAutomator2Options options = new UiAutomator2Options()
    .setPlatformName("Android")
    .setAutomationName("UiAutomator2")
    // ... standard options
    .setCapability("customCapability", "value")
    .setCapability("settings", new Settings()
        .update(new SettingDefinition()));

driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
```

### Q: How do I update Maven dependencies in Docker?

**A:** Edit pom.xml and rebuild:

```bash
# 1. Update pom.xml locally
# Add or update dependency

# 2. Rebuild Docker image (refreshes pom)
docker build --no-cache -t appium-test:latest .

# 3. Run with new image
docker-compose up --build
```

### Q: Can I run multiple Appium servers?

**A:** Yes, for isolation or load balancing:

```yaml
version: '3.8'
services:
  appium1:
    build: .
    ports:
      - "4723:4723"
    environment:
      - APPIUM_PORT=4723

  appium2:
    build: .
    ports:
      - "4724:4723"
    environment:
      - APPIUM_PORT=4724

  appium3:
    build: .
    ports:
      - "4725:4723"
    environment:
      - APPIUM_PORT=4724
```

Tests connect to different ports.

### Q: How do I restart services without rebuilding?

**A:** Use compose stop/start:

```bash
# Stop all (keep containers)
docker-compose stop

# Start again (reuse containers)
docker-compose start

# Vs. down/up (removes containers)
docker-compose down
docker-compose up
```

---

## Getting Help

If issues persist:

1. **Check logs comprehensively:**
```bash
docker-compose logs -f --timestamps appium-server
docker-compose logs -f --timestamps android-emulator
```

2. **Test connectivity:**
```bash
curl -v http://localhost:4723/wd/hub/status
docker-compose exec appium-server adb devices
```

3. **Verify configuration:**
```bash
docker-compose config
docker inspect appium-server | head -50
```

4. **Search documentation:**
- DOCKER_SETUP.md - Comprehensive guide
- DOCKER_COMMANDS_REFERENCE.md - Quick reference
- DOCKER_ARCHITECTURE.md - Architecture diagrams

5. **Report issues with context:**
```bash
# Collect diagnostic information
docker-compose ps > diagnostics.txt
docker system df >> diagnostics.txt
docker logs appium-server >> diagnostics.txt
docker inspect appium-server >> diagnostics.txt
```
