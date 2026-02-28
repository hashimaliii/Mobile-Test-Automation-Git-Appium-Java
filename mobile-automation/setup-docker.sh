#!/bin/bash

# Appium Docker Setup - Installation and Quick Start Guide
# This script automates the setup and initialization of Appium in Docker

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Appium Docker Setup${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
check_prerequisites() {
    echo -e "${YELLOW}Checking prerequisites...${NC}"
    
    if ! command_exists docker; then
        echo -e "${RED}✗ Docker is not installed${NC}"
        echo "Please install Docker from https://docs.docker.com/get-docker/"
        exit 1
    else
        echo -e "${GREEN}✓ Docker is installed${NC}"
        docker --version
    fi
    
    if ! command_exists docker-compose; then
        echo -e "${RED}✗ Docker Compose is not installed${NC}"
        echo "Please install Docker Compose from https://docs.docker.com/compose/install/"
        exit 1
    else
        echo -e "${GREEN}✓ Docker Compose is installed${NC}"
        docker-compose --version
    fi
    
    echo ""
}

# Check if running on Mac for KVM warning
check_platform() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo -e "${YELLOW}Note: Running on macOS. For full emulator support, consider using cloud-based Appium services.${NC}"
        echo ""
    fi
}

# Create .env file if it doesn't exist
setup_env_file() {
    echo -e "${YELLOW}Setting up environment configuration...${NC}"
    
    if [ ! -f .env ]; then
        cp .env.template .env
        echo -e "${GREEN}✓ Created .env file${NC}"
        echo "  Please review and update .env with your configuration"
    else
        echo -e "${GREEN}✓ .env file already exists${NC}"
    fi
    
    echo ""
}

# Build Docker image
build_image() {
    echo -e "${YELLOW}Building Docker image...${NC}"
    
    if docker build -t appium-test:latest . ; then
        echo -e "${GREEN}✓ Docker image built successfully${NC}"
    else
        echo -e "${RED}✗ Failed to build Docker image${NC}"
        exit 1
    fi
    
    echo ""
}

# Show image information
show_image_info() {
    echo -e "${YELLOW}Docker image information:${NC}"
    docker images appium-test:latest
    echo ""
}

# Display next steps
show_next_steps() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${GREEN}Setup complete!${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${YELLOW}Next steps:${NC}"
    echo ""
    echo "1. Review environment configuration:"
    echo -e "   ${BLUE}cat .env${NC}"
    echo ""
    echo "2. Start the containers:"
    echo -e "   ${BLUE}docker-compose up${NC}"
    echo ""
    echo "3. Run tests:"
    echo -e "   ${BLUE}docker-compose exec appium-server mvn test${NC}"
    echo ""
    echo "4. View logs:"
    echo -e "   ${BLUE}docker-compose logs -f${NC}"
    echo ""
    echo "5. Stop containers:"
    echo -e "   ${BLUE}docker-compose down${NC}"
    echo ""
    echo -e "${YELLOW}Available Make targets:${NC}"
    echo "   ${BLUE}make help${NC}      - Show all available commands"
    echo "   ${BLUE}make build${NC}     - Build Docker image"
    echo "   ${BLUE}make up${NC}        - Start containers"
    echo "   ${BLUE}make logs${NC}      - View logs"
    echo "   ${BLUE}make test${NC}      - Run tests"
    echo "   ${BLUE}make down${NC}      - Stop containers"
    echo "   ${BLUE}make clean${NC}     - Remove containers and images"
    echo ""
}

# Main execution
main() {
    check_prerequisites
    check_platform
    setup_env_file
    
    read -p "Build Docker image now? (y/n) " -n 1 -r
    echo ""
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        build_image
        show_image_info
    fi
    
    show_next_steps
}

# Run main function
main
