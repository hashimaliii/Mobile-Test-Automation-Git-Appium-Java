# Dockerize Appium Setup Guide

This guide provides comprehensive instructions for containerizing the Mobile Test Automation project with Appium.

## Overview

The Docker setup includes:
- Appium test automation server
- Android SDK tools
- Java 17 runtime
- Maven build system
- Automated test execution

## Prerequisites

### System Requirements

- **Linux (Recommended for Full Emulator Support):**
  - Docker and Docker Compose installed
  - KVM support enabled (for Android emulator in containers)
  - Minimum 8GB RAM
  - Minimum 10GB disk space

- **macOS/Windows:**
  - Docker Desktop installed
  - For full emulator support, use cloud-based Appium services (Sauce Labs, BrowserStack, etc.)
  - Or use this setup with local/cloud Android devices

### Check Prerequisites

```bash
# Verify Docker installation
docker --version
docker-compose --version

# Check KVM support (Linux only)
grep -cw vmx /proc/cpuinfo  # Intel
grep -cw svm /proc/cpuinfo  # AMD
# Non-zero output = KVM supported
```

## Quick Start

### 1. Build Docker Image

```bash
cd mobile-automation
docker build -t appium-test:latest .
```

### 2. Run with Docker Compose

```bash
# Using the standard compose file (for cloud-based testing)
docker-compose up --build

# Or, for local emulator testing (Linux only)
docker-compose -f docker-compose.amd64-android.yml up --build
```

### 3. View Logs

```bash
# Real-time logs
docker-compose logs -f

# Logs from specific service
docker-compose logs -f appium-server
```

### 4. Stop Containers

```bash
docker-compose down

# Also remove volumes
docker-compose down -v
```

## Configuration Details

### Environment Variables

Edit `docker-compose.yml` to customize:

```yaml
environment:
  - APPIUM_HOST=0.0.0.0
  - APPIUM_PORT=4723
  - APPIUM_LOG_LEVEL=info
  - ANDROID_API_LEVEL=33
```

### Port Mappings

- **4723**: Appium server WebDriver endpoint
- **5554**: Android emulator console (adb)
- **5555**: Android emulator ADB connection

### Volume Mounts

- `./src:/workspace/src` - Test source code
- `./app:/workspace/app` - APK files
- `./target:/workspace/target` - Build output

## Usage Scenarios

### Scenario 1: Run Tests with Cloud-based Devices (macOS/Windows)

1. Modify `BaseTest.java` to use cloud service (Sauce Labs/BrowserStack):

```java
URL appiumURL = new URL("https://ondemand.saucelabs.com/wd/hub");
// Or
URL appiumURL = new URL("https://<username>:<password>@hub.browserstack.com/wd/hub");
```

2. Update capabilities with cloud identifiers

3. Run tests:

```bash
docker-compose up --build
```

### Scenario 2: Run Tests with Local Emulator (Linux)

1. Start the complete stack:

```bash
docker-compose -f docker-compose.amd64-android.yml up --build
```

2. The emulator will start automatically and tests will run

3. Monitor progress:

```bash
docker-compose logs -f appium-server
```

### Scenario 3: Interactive Testing/Debugging

1. Start the container without running tests:

```bash
docker run -it --rm \
  -p 4723:4723 \
  -p 5554:5554 \
  -p 5555:5555 \
  --name appium-debug \
  appium-test:latest /bin/bash
```

2. Inside the container, start Appium:

```bash
appium --address 0.0.0.0 --port 4723 --log-level debug
```

3. In another terminal, connect emulator and run tests:

```bash
docker exec -it appium-debug bash
cd /workspace
mvn test
```

### Scenario 4: CI/CD Integration (GitHub Actions)

```yaml
name: Mobile Tests with Docker

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v1
      
      - name: Build and run tests
        run: |
          docker-compose up --build
      
      - name: Upload test reports
        if: always()
        uses: actions/upload-artifact@v2
        with:
          name: test-reports
          path: target/surefire-reports/
```

## Customization

### Modify Java Version

Edit `Dockerfile`:

```dockerfile
# Change from openjdk-17-jdk to:
openjdk-21-jdk
# and update JAVA_HOME accordingly
```

### Add Custom Dependencies

Edit `pom.xml`:

```xml
<dependency>
    <groupId>your.group</groupId>
    <artifactId>your-artifact</artifactId>
    <version>1.0</version>
</dependency>
```

Rebuild image:

```bash
docker build --no-cache -t appium-test:latest .
```

### Change Android API Level

Edit `Dockerfile`:

```dockerfile
# Change system-images line to desired API level (e.g., API 31, 32, 33, 34)
"system-images;android-34;google_apis;x86_64" 
```

### Adjust Appium Log Level

Edit `docker-compose.yml`:

```yaml
environment:
  - APPIUM_LOG_LEVEL=debug  # or info, warn, error
```

## Troubleshooting

### Issue: "KVM not supported"

**Solution:** 
- Run on Linux host with KVM enabled
- Or switch to cloud-based Appium service

### Issue: "Emulator not starting"

**Solution:**
```bash
# Check emulator logs
docker logs container_name

# Increase timeout in docker-compose.yml:
healthcheck:
  start_period: 120s
```

### Issue: "Out of memory"

**Solution:**
```bash
# Increase Docker memory in docker-compose.yml
deploy:
  resources:
    limits:
      memory: 4G
```

### Issue: "Appium server not connecting"

**Solution:**
```bash
# Check if Appium is running
docker exec appium-server curl http://localhost:4723/wd/hub/status

# Check port mapping
docker ps  # Verify ports are mapped correctly
```

### Issue: "Test timeout"

**Solution:**
Edit `BaseTest.java`:

```java
UiAutomator2Options options = new UiAutomator2Options()
    .setAppWaitDuration(Duration.ofSeconds(120))  // Increase from 60
    // ... other options
```

## Performance Tips

1. **Use volume mounts efficiently:**
   ```bash
   # Exclude target directory from build context
   # Check .dockerignore
   ```

2. **Enable caching in CI/CD:**
   ```bash
   docker build --cache-from appium-test:latest -t appium-test:latest .
   ```

3. **Use multi-stage builds:** (Already implemented in Dockerfile)
   - Separates build and runtime
   - Reduces final image size

4. **Monitor resource usage:**
   ```bash
   docker stats
   ```

## Advanced: Running Multiple Test Suites

Create separate test services in `docker-compose.yml`:

```yaml
services:
  appium-server:
    # ... existing configuration

  login-tests:
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      appium-server:
        condition: service_healthy
    environment:
      - TEST_CLASS=tests.LoginTest
    command: mvn test -Dtest=tests.LoginTest

  product-tests:
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      appium-server:
        condition: service_healthy
    environment:
      - TEST_CLASS=tests.ProductBrowsingTest
    command: mvn test -Dtest=tests.ProductBrowsingTest
```

Run parallel tests:

```bash
docker-compose up --build
```

## Security Best Practices

1. **Do not commit sensitive data:**
   ```bash
   # Add to .gitignore
   .env
   .env.local
   secrets/
   ```

2. **Use environment files for secrets:**
   ```bash
   # Create .env file (add to .gitignore)
   CLOUD_USERNAME=your_username
   CLOUD_ACCESS_KEY=your_key
   
   # Reference in docker-compose.yml
   environment:
     - CLOUD_USERNAME=${CLOUD_USERNAME}
     - CLOUD_ACCESS_KEY=${CLOUD_ACCESS_KEY}
   ```

3. **Run container as non-root (optional):**
   ```dockerfile
   RUN useradd -m -s /bin/bash appium
   USER appium
   ```

4. **Scan image for vulnerabilities:**
   ```bash
   # Using trivy
   trivy image appium-test:latest
   ```

## Additional Resources

- [Appium Documentation](http://appium.io/docs/en/2.0/)
- [Docker Documentation](https://docs.docker.com/)
- [Android SDK Documentation](https://developer.android.com/studio/command-line)
- [Maven Documentation](https://maven.apache.org/guides/)

## Support

For issues or questions:

1. Check Docker logs: `docker logs container_name`
2. Check Appium logs: `docker logs appium-server --tail 100`
3. Verify connectivity: `docker exec container_name curl http://localhost:4723/wd/hub/status`
4. Review test results: `./target/surefire-reports/`
