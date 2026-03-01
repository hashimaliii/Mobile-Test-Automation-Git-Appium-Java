# Stage 1: Build stage
FROM ubuntu:22.04 as builder

ENV DEBIAN_FRONTEND=noninteractive

# Install Java 17 and build tools
RUN apt-get update -o Acquire::Retries=3 -qq && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    maven \
    curl \
    wget \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /build
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage - Appium Server with Android Emulator support
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV APPIUM_HOME=/opt/appium
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH="/opt/android-sdk/platform-tools:/opt/android-sdk/tools/bin:/opt/android-sdk/emulator:${PATH}"
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Install dependencies
RUN apt-get clean \
    && apt-get update -o Acquire::Retries=3 -qq && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    maven \
    curl \
    wget \
    gnupg \
    git \
    unzip \
    libc6-i386 \
    lib32stdc++6 \
    lib32gcc1 \
    libxrender1 \
    libxrandr2 \
    libxinerama1 \
    libxi6 \
    libx11-6 \
    libgl1-mesa-glx \
    xvfb \
    qemu-kvm \
    qemu-system-x86-64 \
    libvirt-daemon-system \
    libvirt-clients \
    bridge-utils \
    python3 \
    python3-pip \
    nodejs \
    npm \
    supervisor \
    netcat \
    telnet \
    && rm -rf /var/lib/apt/lists/*

# Install Node.js Appium
RUN npm install -g appium \
    && npm install -g appium-doctor

# Create working directories
RUN mkdir -p /opt/android-sdk \
    && mkdir -p /workspace \
    && mkdir -p /opt/appium

# Install Android SDK
RUN cd /opt/android-sdk && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip commandlinetools-linux-9477386_latest.zip && \
    rm commandlinetools-linux-9477386_latest.zip && \
    mv cmdline-tools cmdline-tools-latest && \
    mkdir -p cmdline-tools && \
    mv cmdline-tools-latest cmdline-tools/latest

# Accept Android SDK licenses
RUN echo "y" | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager --licenses || true

# Install Android components (API 33 for the test app)
RUN /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager \
    "platform-tools" \
    "platforms;android-33" \
    "system-images;android-33;google_apis;x86_64" \
    "emulator" \
    "tools" \
    || true

# Copy project files
COPY --from=builder /build /workspace
WORKDIR /workspace

# Create entrypoint script
RUN echo '#!/bin/bash\n\
    set -e\n\
    echo "Starting Appium server..."\n\
    appium --address 0.0.0.0 --port 4723 --log-level info &\n\
    APPIUM_PID=$!\n\
    echo "Appium server started with PID: $APPIUM_PID"\n\
    sleep 5\n\
    echo "Running tests..."\n\
    mvn test\n\
    TEST_RESULT=$?\n\
    kill $APPIUM_PID || true\n\
    exit $TEST_RESULT\n\
    ' > /entrypoint.sh && chmod +x /entrypoint.sh

# Expose Appium port
EXPOSE 4723

# Expose emulator ports
EXPOSE 5554 5555

ENTRYPOINT ["/entrypoint.sh"]
