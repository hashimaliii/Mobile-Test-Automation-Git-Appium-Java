.PHONY: help build up down logs test clean rebuild push

help:
	@echo "Mobile Appium Test Automation - Docker Commands"
	@echo ""
	@echo "Available targets:"
	@echo "  build          - Build Docker image"
	@echo "  up             - Start containers with docker-compose"
	@echo "  down           - Stop and remove containers"
	@echo "  logs           - View container logs (real-time)"
	@echo "  logs-server    - View Appium server logs only"
	@echo "  test           - Run tests inside container"
	@echo "  shell          - Access container shell for debugging"
	@echo "  clean          - Remove containers, images, and volumes"
	@echo "  rebuild        - Rebuild image from scratch (no cache)"
	@echo "  push           - Push image to registry (requires REGISTRY variable)"
	@echo "  status         - Check Appium server status"
	@echo "  emulator-logs  - View emulator logs"
	@echo ""
	@echo "Usage examples:"
	@echo "  make build"
	@echo "  make up"
	@echo "  make logs"
	@echo "  make test"
	@echo "  make clean"
	@echo ""

build:
	@echo "Building Docker image..."
	docker build -t appium-test:latest .
	@echo "Build complete!"

up:
	@echo "Starting containers..."
	docker-compose up --build

up-detach:
	@echo "Starting containers in background..."
	docker-compose up --build -d
	@echo "Containers started. Use 'make logs' to view output"

down:
	@echo "Stopping containers..."
	docker-compose down

down-clean:
	@echo "Stopping containers and removing volumes..."
	docker-compose down -v

logs:
	docker-compose logs -f

logs-server:
	docker-compose logs -f appium-server

test:
	@echo "Running tests..."
	docker-compose exec appium-server mvn test

shell:
	@echo "Opening container shell..."
	docker-compose exec appium-server /bin/bash

clean:
	@echo "Cleaning up Docker resources..."
	docker-compose down -v
	docker rmi appium-test:latest || true
	@echo "Cleanup complete!"

rebuild:
	@echo "Rebuilding image without cache..."
	docker build --no-cache -t appium-test:latest .
	@echo "Rebuild complete!"

push:
	@echo "Pushing image to registry..."
	ifndef REGISTRY
		$(error REGISTRY not set. Usage: make push REGISTRY=your.registry.com)
	endif
	docker tag appium-test:latest $(REGISTRY)/appium-test:latest
	docker push $(REGISTRY)/appium-test:latest
	@echo "Push complete!"

status:
	@echo "Checking Appium server status..."
	@curl -s http://localhost:4723/wd/hub/status | python3 -m json.tool || echo "Appium server not responding"

emulator-logs:
	docker-compose logs -f android-emulator

ps:
	@echo "Running containers:"
	docker-compose ps

image-info:
	@echo "Image information:"
	docker images appium-test:latest
	@echo ""
	@echo "Image size:"
	docker inspect appium-test:latest | grep -i size

lint-docker:
	@echo "Linting Dockerfile..."
	docker run --rm -i hadolint/hadolint < Dockerfile

prune:
	@echo "Pruning unused Docker resources..."
	docker system prune -f
	@echo "Prune complete!"

version:
	@echo "Component versions:"
	@echo "Docker:"
	docker --version
	@echo "Docker Compose:"
	docker-compose --version
