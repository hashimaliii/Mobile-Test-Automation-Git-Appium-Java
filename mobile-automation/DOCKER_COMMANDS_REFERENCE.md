# Quick Reference Guide for Docker Commands

## Building

```bash
# Build image
docker build -t appium-test:latest .

# Build with specific tag
docker build -t appium-test:v1.0 .

# Build without cache
docker build --no-cache -t appium-test:latest .
```

## Running Containers

```bash
# Start services with logs
docker-compose up

# Start in background
docker-compose up -d

# Build and start
docker-compose up --build

# Start specific service
docker-compose up appium-server
```

## Viewing Logs

```bash
# View all logs (real-time)
docker-compose logs -f

# View specific service logs
docker-compose logs -f appium-server

# Last 100 lines
docker-compose logs --tail 100

# With timestamps
docker-compose logs -f --timestamps
```

## Execute Commands

```bash
# Run tests
docker-compose exec appium-server mvn test

# Open bash shell
docker-compose exec appium-server /bin/bash

# Run specific test class
docker-compose exec appium-server mvn test -Dtest=tests.LoginTest

# Run with Maven clean
docker-compose exec appium-server mvn clean test
```

## Stopping & Cleanup

```bash
# Stop containers (keep volumes)
docker-compose stop

# Stop and remove containers
docker-compose down

# Remove containers and volumes
docker-compose down -v

# Remove everything (images, containers, volumes)
docker-compose down -v --remove-orphans
docker rmi appium-test:latest
```

## Debugging

```bash
# Check container status
docker-compose ps

# View container processes
docker-compose top appium-server

# Check Appium server health
curl http://localhost:4723/wd/hub/status

# View Docker system info
docker system info

# Check resource usage
docker stats

# View container details
docker inspect appium-server
```

## Image Management

```bash
# List images
docker images | grep appium

# Remove image
docker rmi appium-test:latest

# Tag image
docker tag appium-test:latest myregistry.com/appium-test:v1.0

# Push image
docker push myregistry.com/appium-test:v1.0

# Pull image
docker pull myregistry.com/appium-test:v1.0
```

## Using Makefile (Recommended)

```bash
# Show all available targets
make help

# Build image
make build

# Start containers
make up

# View logs
make logs

# Run tests
make test

# Open shell
make shell

# stop containers
make down

# Complete cleanup
make clean

# Rebuild without cache
make rebuild

# Check image size
make image-info

# Scan for vulnerabilities
make lint-docker
```

## Running Specific Test Suites

```bash
# Run only LoginTest
docker-compose exec appium-server mvn test -Dtest=tests.LoginTest

# Run LoginTest and ProductBrowsingTest
docker-compose exec appium-server mvn test -Dtest=tests.LoginTest,tests.ProductBrowsingTest

# Run all tests with specific pattern
docker-compose exec appium-server mvn test -Dtest=*Test

# Run with specific parameters
docker-compose exec appium-server mvn test \
  -Dtest=tests.LoginTest \
  -DargLine="-Xmx1024m" \
  -Dgroups="fast"
```

## Parallel Testing

```bash
# Run tests in parallel (3 threads)
docker-compose exec appium-server mvn test \
  -DthreadCount=3 \
  -DparallelTestClasses=true

# Run with surefire plugin
docker-compose exec appium-server mvn test \
  -DreuseForks=true \
  -DthreadCount=2
```

## Using Make with Environment Variables

```bash
# Specify custom registry
make push REGISTRY=docker.io/myusername

# Set custom compose file
COMPOSE_FILE=docker-compose.custom.yml make up

# Override environment
APPIUM_LOG_LEVEL=debug make up
```

## Docker Network Commands

```bash
# List networks
docker network ls

# Inspect network
docker network inspect appium-network

# Check container IP
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name
```

## Volume Management

```bash
# List volumes
docker volume ls

# Inspect volume
docker volume inspect volume_name

# Remove volume
docker volume rm volume_name

# Remove unused volumes
docker volume prune
```

## Useful One-Liners

```bash
# Remove all appium containers
docker rm $(docker ps -aq -f "ancestor=appium-test:latest")

# Remove all dangling images
docker image prune -f

# View all running containers
docker ps --format "table {{.ID}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"

# Get container IP address
docker-compose exec appium-server hostname -I

# Check Docker disk usage
docker system df

# Follow logs with timestamps
docker-compose logs -f --timestamps appium-server

# Run container with environment override
docker run -e APPIUM_LOG_LEVEL=debug appium-test:latest
```

## CI/CD Integration

```bash
# Run with build artifacts
docker-compose up --build --exit-code-from appium-server

# Build specific dockerfile
docker build -t appium-emulator:latest -f Dockerfile.emulator .

# Tag for CI/CD pipeline
docker tag appium-test:latest ci-registry.example.com/appium-test:${CI_COMMIT_SHA}

# Push with CI/CD tag
docker push ci-registry.example.com/appium-test:${CI_COMMIT_SHA}
```

## Troubleshooting Commands

```bash
# Check if port is in use
docker ps -a --filter "expose=4723"

# View container network settings
docker network inspect bridge

# Check container resource limits
docker inspect --format='{{.HostConfig.Memory}}' container_name

# View container environment variables
docker inspect --format='{{json .Config.Env}}' container_name | jq

# Check container health
docker inspect --format='{{.State.Health.Status}}' container_name

# Get container exit code
docker inspect --format='{{.State.ExitCode}}' container_name
```

## Performance Monitoring

```bash
# Monitor in real-time
docker stats --no-stream=false

# Monitor specific container
docker stats appium-server

# Show resource history
docker stats --format "table {{.Container}}\t{{.MemUsage}}\t{{.CPUPerc}}"
```
