# Appium Docker Setup - Complete README

## Overview

This document provides a complete guide for containerizing the Mobile Test Automation project with Appium using Docker.

## What's Included

### Docker Files
- **Dockerfile** - Multi-stage build with Java 17, Maven, Appium, and Android SDK
- **Dockerfile.emulator** - Optional Android emulator container image
- **docker-compose.yml** - Standard development configuration
- **docker-compose.prod.yml** - Production-ready configuration with resource limits and security
- **docker-compose.amd64-android.yml** - Complete stack with emulator (Linux KVM required)

### Configuration & Scripts
- **.env.template** - Environment configuration template
- **.dockerignore** - Excludes unnecessary files from Docker build
- **setup-docker.sh** - Automated setup script
- **Makefile** - Convenient command shortcuts

### Documentation
- **DOCKER_SETUP.md** - Comprehensive setup and configuration guide
- **DOCKER_COMMANDS_REFERENCE.md** - Quick reference for Docker commands
- **.github/workflows/docker-tests.yml** - GitHub Actions CI/CD pipeline

## Quick Start

### 1. Prerequisites
- Docker (version 20.10+)
- Docker Compose (version 1.29+)
- Linux (for emulator support), or cloud Appium service

### 2. Setup

```bash
# Make setup script executable (Linux/macOS)
chmod +x setup-docker.sh

# Run setup script
./setup-docker.sh
```

Or manually:

```bash
# Copy environment template
cp .env.template .env

# Build Docker image
docker build -t appium-test:latest .

# Start containers
docker-compose up --build
```

### 3. Run Tests

```bash
# Option 1: Using docker-compose
docker-compose exec appium-server mvn test

# Option 2: Using Make
make test

# Option 3: View logs while tests run
make logs
```

## Usage Examples

### Development Workflow

```bash
# Start containers in background
make up-detach

# View logs
make logs

# Run all tests
make test

# Run specific test class
docker-compose exec appium-server mvn test -Dtest=tests.LoginTest

# Open interactive shell
make shell

# Stop containers
make down
```

### Production Deployment

```bash
# Use production compose file
docker-compose -f docker-compose.prod.yml up -d

# Monitor health
docker-compose ps

# Check logs
docker-compose logs -f appium-server

# Scale services (if using swarm)
docker service scale appium=3
```

### CI/CD Integration

GitHub Actions workflow automatically:
- Builds Docker image
- Runs all tests
- Uploads test results
- Performs security scanning
- Provides PR comments with results

Enable by pushing to `main` or `develop` branches.

## Key Features

✅ Multi-stage Docker build for optimized image size
✅ Java 17, Maven, Android SDK pre-installed
✅ Appium server with UiAutomator2
✅ Health checks for service reliability
✅ Resource limits and security hardening
✅ Volume mounts for code/test isolation
✅ Docker Compose for easy orchestration
✅ Makefile for convenient commands
✅ GitHub Actions CI/CD integration
✅ Comprehensive documentation

## Configuration

### Environment Variables

Edit `.env` to customize:

```bash
APPIUM_HOST=0.0.0.0
APPIUM_PORT=4723
APPIUM_LOG_LEVEL=info
ANDROID_API_LEVEL=33
TEST_TIMEOUT=300
```

### Resource Limits

In `docker-compose.prod.yml`:

```yaml
deploy:
  resources:
    limits:
      cpus: '2'
      memory: 4G
    reservations:
      cpus: '1'
      memory: 2G
```

### Port Mappings

- **4723** - Appium server WebDriver endpoint
- **5554** - Android emulator console
- **5555** - Android ADB connection

## Troubleshooting

### Common Issues

**Port already in use:**
```bash
# Kill process on port 4723
lsof -ti:4723 | xargs kill -9

# Or use different port
docker-compose ups -e APPIUM_PORT=4724
```

**Out of memory:**
```bash
# Increase Docker memory in settings
# Or in docker-compose.yml:
deploy:
  resources:
    limits:
      memory: 8G
```

**Tests timeout:**
```bash
# Increase timeout in BaseTest.java
setAppWaitDuration(Duration.ofSeconds(120))

# Or via environment
TEST_TIMEOUT=600
```

### Debug Mode

```bash
# Run with debug logging
docker-compose -e APPIUM_LOG_LEVEL=debug up

# Access container shell
docker-compose exec appium-server /bin/bash

# Check Appium status
docker-compose exec appium-server curl http://localhost:4723/wd/hub/status
```

## Performance Tips

1. **Use volume mounts efficiently** - Exclude unnecessary files with `.dockerignore`
2. **Enable caching in CI/CD** - Leverage Docker layer caching
3. **Parallel test execution** - Configure Maven surefire for multiple threads
4. **Monitor resource usage** - Use `docker stats`
5. **Keep images small** - Use multi-stage builds

## Security Best Practices

✓ Run containers as non-root (optional)
✓ Use read-only volumes where possible
✓ Limit resource usage
✓ Scan images with Trivy
✓ Don't commit secrets to git
✓ Use `.env` files for sensitive data
✓ Enable security options (no-new-privileges)

## File Structure

```
mobile-automation/
├── Dockerfile                          # Main Docker image
├── Dockerfile.emulator                 # Optional emulator image
├── docker-compose.yml                  # Development setup
├── docker-compose.prod.yml             # Production setup
├── docker-compose.amd64-android.yml    # With emulator (Linux)
├── .dockerignore                       # Files to exclude
├── .env.template                       # Configuration template
├── setup-docker.sh                     # Setup script
├── Makefile                            # Command shortcuts
├── DOCKER_SETUP.md                     # Comprehensive guide
├── DOCKER_COMMANDS_REFERENCE.md        # Quick reference
├── .github/workflows/docker-tests.yml  # GitHub Actions
├── pom.xml                             # Maven config
├── app/                                # APK files
├── src/                                # Source code
│   └── test/
│       └── java/
│           ├── base/BaseTest.java     # Base test class
│           └── tests/                 # Test classes
└── target/                             # Build output
```

## Make Commands

```bash
make help          # Show available commands
make build         # Build Docker image
make up            # Start containers
make logs          # View logs
make test          # Run tests
make shell         # Open shell
make down          # Stop containers
make clean         # Remove containers/images
make rebuild       # Rebuild without cache
```

## Advanced Topics

### Running with Sauce Labs

```bash
# Set credentials
export SAUCE_USERNAME=your_username
export SAUCE_ACCESS_KEY=your_key

# Update BaseTest.java
URL appiumURL = new URL("https://ondemand.saucelabs.com/wd/hub");

# Run tests
docker-compose up --build
```

### Parallel Test Execution

```bash
docker-compose exec appium-server mvn test \
  -DthreadCount=3 \
  -DparallelTestClasses=true
```

### Custom Test Suite

```bash
docker-compose exec appium-server mvn test \
  -Dtest=tests.LoginTest,tests.CartTest
```

### Generate HTML Report

```bash
docker-compose exec appium-server mvn surefire-report:report
docker-compose cp appium-server:/workspace/target/site ./test-report
```

## Support & Resources

- [Appium Documentation](http://appium.io/docs/en/2.0/)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Android SDK Documentation](https://developer.android.com/studio/command-line)
- [Maven Documentation](https://maven.apache.org/)

## Contributing

When adding new features:

1. Update Dockerfile if adding dependencies
2. Update docker-compose.yml if adding services
3. Test locally with `make up` and `make test`
4. Document changes in DOCKER_SETUP.md
5. Run `docker-compose down -v` to clean up

## License

MIT License - See LICENSE file for details
