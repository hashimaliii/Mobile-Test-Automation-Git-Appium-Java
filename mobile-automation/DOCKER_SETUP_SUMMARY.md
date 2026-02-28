# Docker Setup Summary

## Files Created

This directory now contains a complete Docker setup for Appium test automation.

### Docker Build Files

| File | Purpose |
|------|---------|
| `Dockerfile` | Main multi-stage Docker image with Java 17, Maven, Appium, and Android SDK |
| `Dockerfile.emulator` | Optional standalone Android emulator container |
| `.dockerignore` | Exclude unnecessary files from Docker build context |

### Docker Compose Files

| File | Purpose |
|------|---------|
| `docker-compose.yml` | Standard development configuration with Appium server |
| `docker-compose.prod.yml` | Production-ready with resource limits and security hardening |
| `docker-compose.amd64-android.yml` | Complete stack including Android emulator (Linux KVM required) |
| `docker-compose.nginx.yml` | Setup with NGINX reverse proxy for remote access |

### Configuration Files

| File | Purpose |
|------|---------|
| `.env.template` | Environment variable configuration template |
| `nginx.conf` | NGINX reverse proxy configuration for remote access |

### Build & Automation Scripts

| File | Purpose |
|------|---------|
| `setup-docker.sh` | Automated setup script to check prerequisites and build image |
| `Makefile` | Convenient Make targets for common Docker commands |

### Documentation

| File | Purpose | Notes |
|------|---------|-------|
| `DOCKER_README.md` | Complete overview and quick start guide | Start here |
| `DOCKER_SETUP.md` | Comprehensive setup and configuration guide | Detailed instructions |
| `DOCKER_COMMANDS_REFERENCE.md` | Quick reference for Docker commands | Copy-paste commands |

### CI/CD Integration

| File | Purpose |
|------|---------|
| `.github/workflows/docker-tests.yml` | GitHub Actions workflow for automated testing and security scanning |

## Quick Start Commands

```bash
# 1. Setup (interactive)
chmod +x setup-docker.sh
./setup-docker.sh

# 2. Start containers
make up              # or: docker-compose up --build

# 3. Run tests
make test            # or: docker-compose exec appium-server mvn test

# 4. View logs
make logs            # or: docker-compose logs -f

# 5. Stop containers
make down            # or: docker-compose down
```

## Development Workflow

```bash
# Start in background
docker-compose up -d

# Run tests
docker-compose exec appium-server mvn test

# Monitor logs
docker-compose logs -f

# Interactive debugging
docker-compose exec appium-server /bin/bash

# Stop everything
docker-compose down -v
```

## Production Deployment

```bash
# Use production compose file
docker-compose -f docker-compose.prod.yml up -d

# With NGINX reverse proxy
docker-compose -f docker-compose.nginx.yml up -d

# Monitor
docker-compose ps
docker stats
```

## Key Features Implemented

✅ **Multi-stage Docker build** - Optimized image size
✅ **Java 17 + Maven** - Full build environment
✅ **Appium + UiAutomator2** - Mobile test automation
✅ **Android SDK** - Platform tools available
✅ **Health checks** - Service reliability verification
✅ **Resource limits** - Memory and CPU constraints
✅ **Security hardening** - Cap drop, read-only volumes
✅ **Docker Compose** - Easy orchestration
✅ **Makefile** - Convenient commands
✅ **GitHub Actions** - CI/CD integration
✅ **NGINX proxy** - Remote access with rate limiting
✅ **Comprehensive docs** - Multiple guides included

## Configuration Options

### Environment Variables (.env)

```bash
APPIUM_HOST=0.0.0.0
APPIUM_PORT=4723
APPIUM_LOG_LEVEL=info
ANDROID_API_LEVEL=33
TEST_TIMEOUT=300
```

### Resource Limits (docker-compose.prod.yml)

```yaml
cpus: '2'
memory: 4G
```

### Port Mappings

- 4723: Appium WebDriver
- 5554: Android emulator console
- 5555: ADB connection
- 80/443: NGINX proxy
- 8080: NGINX health check

## Usage Scenarios

**Development** → Use `docker-compose.yml`
**Production** → Use `docker-compose.prod.yml`
**Remote Access** → Use `docker-compose.nginx.yml`
**With Emulator** → Use `docker-compose.amd64-android.yml` (Linux only)

## Make Commands Available

```
make help              - Show all commands
make build             - Build Docker image
make up                - Start containers
make up-detach         - Start in background
make down              - Stop containers
make down-clean        - Stop and remove volumes
make logs              - View logs (real-time)
make logs-server       - View Appium logs only
make test              - Run tests
make shell             - Open container shell
make clean             - Remove all Docker artifacts
make rebuild           - Rebuild without cache
make push              - Push image to registry
make status            - Check Appium server status
make ps                - Show running containers
make image-info        - Display image size
make lint-docker       - Lint Dockerfile
make prune             - Cleanup unused resources
make version           - Show component versions
```

## Important Notes

### For macOS/Windows Users

Full emulator support requires Linux with KVM. For macOS/Windows:
- Use cloud-based Appium services (Sauce Labs, BrowserStack)
- Modify `BaseTest.java` to connect to cloud service
- Run tests with `docker-compose.yml` (standard config)

### For Linux Users

Full emulator support available:
- Use `docker-compose.amd64-android.yml` for complete stack
- Requires KVM support and adequate disk space (~10GB)
- Check: `grep -cw vmx /proc/cpuinfo` (Intel) or `grep -cw svm /proc/cpuinfo` (AMD)

### Volume Mounts

- `./src` - Test source code (mounted read-only in production)
- `./app` - APK files
- `./target` - Build output and test reports
- Named volumes for persistence

## Troubleshooting

**Port 4723 already in use:**
```bash
docker-compose exec appium-server lsof -ti:4723 | xargs kill -9
```

**Out of memory:**
Edit `docker-compose.yml` and increase memory limit under `deploy.resources.limits.memory`

**Emulator not starting:**
Increase `start_period` in healthcheck from 60s to 120s

**Tests timing out:**
Update `Duration.ofSeconds(30)` in `BaseTest.java` to higher value

## Next Steps

1. Review `DOCKER_README.md` for complete overview
2. Read `DOCKER_SETUP.md` for detailed configuration
3. Check `DOCKER_COMMANDS_REFERENCE.md` for command help
4. Run `./setup-docker.sh` to setup
5. Execute `make up` to start containers
6. Run `make test` to execute tests

## Support Resources

- Appium: http://appium.io/docs/en/2.0/
- Docker: https://docs.docker.com/
- Docker Compose: https://docs.docker.com/compose/
- Android SDK: https://developer.android.com/studio/command-line
- GitHub Actions: https://docs.github.com/en/actions

---

**Complete Docker setup ready for development, testing, and production deployment!**
