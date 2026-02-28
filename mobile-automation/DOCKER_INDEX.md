# Docker Setup - Documentation Index

Complete Docker containerization for Appium mobile test automation.

## 📚 Documentation Files

### Quick Start (Start Here!)
- **[DOCKER_README.md](DOCKER_README.md)** - Complete overview and quick start guide
- **[DOCKER_SETUP_SUMMARY.md](DOCKER_SETUP_SUMMARY.md)** - Summary of all created files

### Configuration & Management
- **[DOCKER_SETUP.md](DOCKER_SETUP.md)** - Comprehensive setup and usage guide
- **[DOCKER_COMMANDS_REFERENCE.md](DOCKER_COMMANDS_REFERENCE.md)** - Quick command reference
- **[DOCKER_ARCHITECTURE.md](DOCKER_ARCHITECTURE.md)** - Architecture diagrams and design

### Problem Solving
- **[DOCKER_TROUBLESHOOTING.md](DOCKER_TROUBLESHOOTING.md)** - Troubleshooting guide and FAQ

---

## 🐳 Docker Files

### Build Configuration
```
Dockerfile              - Multi-stage production build
Dockerfile.emulator     - Optional Android emulator image
.dockerignore           - Excluded files from build
```

### Compose Configurations
```
docker-compose.yml              - Development setup (recommended)
docker-compose.prod.yml         - Production with resource limits
docker-compose.amd64-android.yml - Full stack with emulator (Linux KVM)
docker-compose.nginx.yml        - With NGINX reverse proxy
```

### Configuration & Scripts
```
.env.template           - Environment variable template
nginx.conf              - NGINX reverse proxy configuration
setup-docker.sh         - Interactive setup script
Makefile                - Command shortcuts
```

### CI/CD Integration
```
.github/workflows/docker-tests.yml - GitHub Actions pipeline
```

---

## 🚀 Quick Commands

### Setup (First Time)
```bash
# Interactive setup
chmod +x setup-docker.sh
./setup-docker.sh

# Or manually
cp .env.template .env
docker build -t appium-test:latest .
```

### Development
```bash
# Start containers
docker-compose up --build

# Run tests
docker-compose exec appium-server mvn test

# View logs
docker-compose logs -f

# Stop containers
docker-compose down
```

### Using Make (Easier!)
```bash
make help          # Show all commands
make build         # Build image
make up            # Start containers
make test          # Run tests
make logs          # View logs
make shell         # Open bash
make down          # Stop containers
make clean         # Remove all
```

---

## 📋 Documentation Organization

### By Use Case

**I want to...**
- **Get started quickly** → Read DOCKER_README.md
- **Understand what was created** → Read DOCKER_SETUP_SUMMARY.md
- **Configure for production** → Read DOCKER_SETUP.md + DOCKER_ARCHITECTURE.md
- **Find a specific command** → Read DOCKER_COMMANDS_REFERENCE.md
- **Fix a problem** → Read DOCKER_TROUBLESHOOTING.md
- **See technical architecture** → Read DOCKER_ARCHITECTURE.md

### By Role

**As a Developer:**
1. DOCKER_README.md - Overview
2. DOCKER_SETUP.md - Local configuration
3. DOCKER_COMMANDS_REFERENCE.md - Daily commands
4. DOCKER_TROUBLESHOOTING.md - When things break

**As a DevOps Engineer:**
1. DOCKER_ARCHITECTURE.md - System design
2. DOCKER_SETUP.md - Configuration options
3. docker-compose.prod.yml - Production deployment
4. .github/workflows/docker-tests.yml - CI/CD pipeline

**As a QA Engineer:**
1. DOCKER_README.md - Overview
2. setup-docker.sh - Initial setup
3. Makefile - Run commands
4. DOCKER_TROUBLESHOOTING.md - Issues

---

## 🎯 Key Features

✅ **Multi-stage Docker build** - Optimized ~2-3GB image
✅ **Java 17 + Maven** - Complete build environment  
✅ **Appium + UiAutomator2** - Mobile automation framework
✅ **Android SDK** - Platform tools pre-installed
✅ **Docker Compose** - Easy orchestration
✅ **Health checks** - Service reliability
✅ **Resource limits** - Production-safe (prod config)
✅ **Security hardening** - Capability limits, read-only volumes
✅ **Makefile** - Convenient command shortcuts
✅ **GitHub Actions** - Automated CI/CD
✅ **NGINX proxy** - Remote access capability
✅ **Comprehensive docs** - Multiple guides

---

## 📂 Project Structure

```
mobile-automation/
├── Docker Files          (Dockerfile, docker-compose.yml, etc.)
├── Documentation         (All DOCKER_*.md files)
├── Configuration         (.env.template, nginx.conf, etc.)
├── Automation Scripts    (setup-docker.sh, Makefile)
├── CI/CD Pipeline        (.github/workflows/docker-tests.yml)
├── pom.xml               (Maven configuration)
├── app/                  (APK files)
├── src/                  (Test source code)
│   ├── main/java/
│   └── test/java/
│       ├── base/BaseTest.java
│       └── tests/
│           ├── AppLaunchTest.java
│           ├── AppFunctionalTest.java
│           ├── LoginTest.java
│           ├── ProductBrowsingTest.java
│           ├── CartTest.java
│           ├── NavigationTest.java
│           └── AccessibilityTest.java
└── target/               (Build output)
    └── surefire-reports/ (Test results)
```

---

## 🔧 Configuration Guides

### For Development
→ See **DOCKER_SETUP.md** - "Quick Start" section

### For Production
→ See **DOCKER_SETUP.md** - "Production Deployment" section
→ Use **docker-compose.prod.yml**

### For Cloud Services (Sauce Labs/BrowserStack)
→ See **DOCKER_SETUP.md** - "Scenario 1: Cloud-based Devices"
→ Modify **BaseTest.java** with cloud URL

### For Remote Access
→ See **DOCKER_SETUP.md** - "Advanced: Running with NGINX"
→ Use **docker-compose.nginx.yml**

### For CI/CD Pipeline
→ See **.github/workflows/docker-tests.yml**
→ Automatically builds, tests, and reports results

---

## 🔍 Troubleshooting Quick Links

| Problem | Solution |
|---------|----------|
| Port 4723 already in use | DOCKER_TROUBLESHOOTING.md → "Port Already in Use" |
| Out of memory | DOCKER_TROUBLESHOOTING.md → "Out of Memory" |
| Appium not responding | DOCKER_TROUBLESHOOTING.md → "Appium Not Responding" |
| Tests timeout | DOCKER_TROUBLESHOOTING.md → "Tests Timeout" |
| App won't launch | DOCKER_TROUBLESHOOTING.md → "Test Fails to Launch App" |
| Element not found | DOCKER_TROUBLESHOOTING.md → "Element Not Found" |
| Emulator won't start | DOCKER_TROUBLESHOOTING.md → "Emulator Not Starting" |
| Network issues | DOCKER_TROUBLESHOOTING.md → "Docker Network Issues" |
| Volume issues | DOCKER_TROUBLESHOOTING.md → "Volume Mount Not Working" |

---

## 📊 Architecture Overview

Every component and its role:

```
User Terminal
    │
    ├─→ setup-docker.sh (Setup)
    ├─→ docker-compose (Orchestration)
    └─→ Make commands (Convenience)
            │
            ▼
    Docker Build System
            │
    ┌───────┴───────┐
    ▼               ▼
Docker Image    Named Volumes
(appium-test)   (appium-data)
    │               │
    ▼               ▼
Containers      Persistent Data
├── Appium       ├── Logs
├── Java 17      ├── Config
├── Maven        └── Cache
├── Android SDK
└── Test Code
```

See **DOCKER_ARCHITECTURE.md** for detailed diagrams.

---

## 📈 Performance & Monitoring

**Check resource usage:**
```bash
docker stats                    # Real-time stats
docker system df                # Disk usage
make image-info                 # Image size
```

**Common optimizations:**
- Use volume mounts efficiently (.dockerignore)
- Enable Docker layer caching in CI/CD
- Configure parallel test execution
- Monitor with `docker stats`

See **DOCKER_SETUP.md** - "Performance Tips" section

---

## 🔐 Security Best Practices

Implemented in production config:
- ✅ Resource limits (CPU, memory)
- ✅ Security options (no-new-privileges, cap drop)
- ✅ Read-only volumes where possible
- ✅ Health checks
- ✅ Proper logging
- ✅ NGINX rate limiting

See **DOCKER_SETUP.md** - "Security Best Practices" section

---

## 🎓 Learning Resources

**Official Documentation:**
- [Docker Docs](https://docs.docker.com/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [Appium Docs](http://appium.io/docs/en/2.0/)
- [Android SDK Docs](https://developer.android.com/studio/command-line)
- [Maven Docs](https://maven.apache.org/guides/)

**In This Project:**
- DOCKER_ARCHITECTURE.md - System design diagrams
- DOCKER_COMMANDS_REFERENCE.md - Common Docker commands
- DOCKER_TROUBLESHOOTING.md - FAQ and problem solving

---

## 🆘 Support & Help

### Step-by-Step Diagnostics

```bash
# 1. Check if Docker is working
docker --version
docker ps

# 2. Check images
docker images appium-test:latest

# 3. Check running containers
docker-compose ps

# 4. Check Appium connectivity
curl http://localhost:4723/wd/hub/status

# 5. View detailed logs
docker logs appium-server -f --timestamps

# 6. Check container details
docker inspect appium-server

# 7. Verify volumes
docker volume ls
docker volume inspect appium-data
```

### Get Help

1. **For setup issues** → Read DOCKER_SETUP.md
2. **For syntax help** → Check DOCKER_COMMANDS_REFERENCE.md
3. **For errors** → Search DOCKER_TROUBLESHOOTING.md
4. **For architecture** → Review DOCKER_ARCHITECTURE.md

### Report Issues

Include when asking for help:
- Error message (complete)
- Docker version (`docker --version`)
- Docker Compose version (`docker-compose --version`)
- Output of `docker ps` and `docker logs`
- Relevant docker-compose.yml section

---

## 📝 File-by-File Guide

| File | Purpose | When to Use | Audience |
|------|---------|-----------|----------|
| DOCKER_README.md | Overview & quick start | First time | Everyone |
| DOCKER_SETUP.md | Detailed configuration | Setup & config | Developers, DevOps |
| DOCKER_SETUP_SUMMARY.md | Files created summary | Reference | Everyone |
| DOCKER_COMMANDS_REFERENCE.md | Command cookbook | Daily work | Developers |
| DOCKER_ARCHITECTURE.md | Technical diagrams | Understanding design | DevOps, Architects |
| DOCKER_TROUBLESHOOTING.md | Problems & solutions | When stuck | Everyone |
| Dockerfile | Build specification | CI/CD, deployment | DevOps |
| docker-compose.yml | Development setup | Local development | Developers |
| docker-compose.prod.yml | Production setup | Production deploy | DevOps |
| setup-docker.sh | Automated setup | First time (optional) | Everyone |
| Makefile | Command shortcuts | Daily work | Developers |

---

## 🚢 Version Information

- **Java**: 17
- **Maven**: 3.9.12
- **Appium**: 2.x (latest)
- **UiAutomator2**: 8.6.0
- **Selenium**: 4.10.0
- **TestNG**: 7.8.0
- **Docker**: 20.10+
- **Docker Compose**: 1.29+

---

## ✨ Next Steps

1. **New to Docker?**
   → Start with DOCKER_README.md

2. **Ready to set up?**
   → Run `./setup-docker.sh OR Read DOCKER_SETUP.md

3. **Want to use right away?**
   → Use Makefile: `make build` → `make up` → `make test`

4. **Having issues?**
   → Check DOCKER_TROUBLESHOOTING.md

5. **Need specific commands?**
   → Reference DOCKER_COMMANDS_REFERENCE.md

---

**Complete Docker containerization ready for development, testing, and production!** 🎉
