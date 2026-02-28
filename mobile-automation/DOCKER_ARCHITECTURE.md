# Docker Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Development Laptop                        │
│                    (macOS/Windows/Linux)                         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │  Docker Desktop  │
                    │  (or Docker CLI) │
                    └──────────────────┘
                              │
                ┌─────────────┼──────────────┐
                │             │              │
                ▼             ▼              ▼
        ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
        │  Build Image │ │ Compose      │ │ Execute Cmd  │
        │  docker      │ │ Orchestrate  │ │ docker exec  │
        │  build       │ │ Services     │ │ mvn test     │
        └──────────────┘ └──────────────┘ └──────────────┘
                              │
                ┌─────────────┼──────────────┐
                ▼             ▼              ▼
        ┌──────────────────────────────────┐
        │       Docker Network             │
        │     appium-network               │
        │  172.20.0.0/16                   │
        └──────────────────────────────────┘
                │         │          │
        ┌───────▼──┐  ┌───▼─────┐  ┌───▼────────┐
        │           │  │         │  │            │
        │ Container │  │ Container│ │ Container  │
        │ NGINX     │  │ Appium  │  │ Emulator   │
        │           │  │         │  │            │
        │ Port:     │  │ Port:   │  │ Port:      │
        │ 80, 443   │  │ 4723    │  │ 5554, 5555 │
        │ 8080      │  │         │  │            │
        └───────────┘  └─────────┘  └────────────┘
                │           │             │
                └─────┬─────┴────┬────────┘
                      │          │
                      ▼          ▼
            ┌──────────────┐  ┌──────────────────┐
            │ Host Network │  │ Volume Mounts    │
            │ localhost    │  │ /src, /app,      │
            │              │  │ /target, /logs   │
            └──────────────┘  └──────────────────┘
```

## Service Architecture

### Standard Development Setup (docker-compose.yml)

```
┌─────────────────────────────────────────┐
│        Appium Server Container          │
├─────────────────────────────────────────┤
│  ● Java 17 Runtime                      │
│  ● Maven Build Tool                     │
│  ● Appium Server (Node.js)              │
│  ● Android SDK (platform-tools)         │
│  ● Test Classes (LoginTest, etc.)       │
├─────────────────────────────────────────┤
│  ● Volumes: src, app, target, logs      │
│  ● Port: 4723 (Appium WebDriver)        │
│  ● Health Check: /wd/hub/status         │
│  ● Network: appium-network              │
└─────────────────────────────────────────┘
         │                      │
         ▼                      ▼
    Local Devices        (or Cloud Service)
    (Real/Emulator)      Sauce Labs, BrowserStack
```

### Production Setup (docker-compose.prod.yml)

```
┌──────────────────────────────────────────────────────────┐
│              Docker Network (172.20.0.0/16)              │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  ┌────────────────────────┐   ┌────────────────────┐   │
│  │   Appium Container     │   │  Emulator Container│   │
│  ├────────────────────────┤   ├────────────────────┤   │
│  │ Resource Limits:       │   │ Resource Limits:   │   │
│  │ • CPU: 2 cores        │   │ • CPU: 4 cores     │   │
│  │ • Memory: 4GB         │   │ • Memory: 6GB      │   │
│  │                        │   │                    │   │
│  │ Security:             │   │ Device Access:     │   │
│  │ • No new privs        │   │ • /dev/kvm mounted │   │
│  │ • Read-only volumes   │   │ • Privileged mode  │   │
│  │ • Limited caps        │   │                    │   │
│  │                        │   │ Ports:             │   │
│  │ Port: 4723            │   │ • 5554 (console)   │   │
│  │ Restart: unless-stop  │   │ • 5555 (adb)       │   │
│  │ Logging: json-file    │   │                    │   │
│  └────────────────────────┘   └────────────────────┘   │
│           ▲                              ▲              │
│           └──────────────┬───────────────┘              │
│                          │                              │
│              Communication via Docker Network           │
│              (hostname resolution)                      │
│                                                         │
└──────────────────────────────────────────────────────────┘
         │                                        │
         ├───────┬──────────────────────┬────────┤
         │       │                      │        │
         ▼       ▼                      ▼        ▼
      Logs    Volumes               Health   Metrics
    (10m max) (persistent)          Checks   (docker stats)
```

### Complete Stack with NGINX (docker-compose.nginx.yml)

```
┌──────────────────────────────────────────────────────────┐
│                    External Network                       │
│              (Internet / LAN / HTTPS)                     │
└──────────────────────────────────────────────────────────┘
         │  Port 80, 443, 8080         │
         ▼                             ▼
    ┌─────────────────────────────────────────┐
    │      NGINX Reverse Proxy Container      │
    ├─────────────────────────────────────────┤
    │ • Rate Limiting (10 r/s)                │
    │ • SSL/TLS Termination                   │
    │ • Security Headers                      │
    │ • Load Balancing                        │
    │ • WebSocket Support                     │
    │ • Access Logging                        │
    └─────────────────────────────────────────┘
         │         │           │
         └────┬────┴───────┬───┘
              │            │
              ▼            ▼
          Internal Network (appium-network)
          172.20.0.0/16
              │            │
         ┌────▼──────┐   ┌─▼─────────┐
         │  Appium   │   │ Emulator   │
         │ Container │   │ Container  │
         │(Port 4723)│   │(5554, 5555)│
         └───────────┘   └────────────┘
```

## Data Flow: Test Execution

```
┌───────────────────────────────────┐
│  Test File (LoginTest.java)       │
│  run: mvn test                    │
└──────────────┬────────────────────┘
               │
               ▼
        ┌─────────────────────┐
        │ Maven (in container)│
        │ Compile & Run Tests │
        └──────────┬──────────┘
                   │
                   ▼
        ┌─────────────────────┐
        │  Test Initialization│
        │  BaseTest.setUp()   │
        └──────────┬──────────┘
                   │
                   ▼
    ┌──────────────────────────────┐
    │  Create Appium Session       │
    │  AndroidDriver instance      │
    │  Connect to localhost:4723   │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │  Appium Server (localhost)   │
    │  Receives WebDriver commands │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │  UiAutomator2 Bridge         │
    │  Communicates with device    │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │  Android Device/Emulator     │
    │  Executes app interactions   │
    │  Returns results             │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │  Test Assertions             │
    │  Assert.assertTrue()         │
    │  Reports results             │
    └──────────┬───────────────────┘
               │
               ▼
    ┌──────────────────────────────┐
    │  Surefire Reports            │
    │  target/surefire-reports/    │
    │  HTML & XML reports          │
    └──────────────────────────────┘
```

## Multi-Stage Build Process

```
┌─ Stage 1: Builder ────────────────────────┐
│                                           │
│ FROM ubuntu:22.04 as builder              │
│ │                                         │
│ ├─ Install Java 17                        │
│ ├─ Install Maven                          │
│ ├─ Copy project sources                   │
│ ├─ Run: mvn clean package                 │
│ │                                         │
│ └─ Output: compiled JAR, dependencies     │
│                                           │
└─ Discard (not in final image)─────────────┘
    │
    │ Copy compiled artifacts
    ▼
┌─ Stage 2: Runtime ────────────────────────┐
│                                           │
│ FROM ubuntu:22.04 (fresh)                 │
│ │                                         │
│ ├─ Install Java 17                        │
│ ├─ Install Appium & dependencies          │
│ ├─ Install Android SDK                    │
│ ├─ Install Maven (for test execution)     │
│ ├─ Copy artifacts from builder            │
│ ├─ Copy source code                       │
│ │                                         │
│ └─ Result: Slim, optimized production... │  
│           image with only runtime needs  │
│                                           │
└───────────────────────────────────────────┘
    │
    └─ Final image size: ~2-3GB
       (vs ~5GB+ if single stage)
```

## Container Lifecycle

```
┌─────────────────────────────────────┐
│       docker-compose up             │
│  (or docker build + docker run)     │
└─────────────┬───────────────────────┘
              │
              ▼
    ┌──────────────────────┐
    │   Image Pulling      │
    │  (if not local)      │
    └──────────┬───────────┘
               │
               ▼
    ┌──────────────────────┐
    │  Container Creation  │
    │  • Mount volumes     │
    │  • Set environment   │
    │  • Configure network │
    └──────────┬───────────┘
               │
               ▼
    ┌──────────────────────┐
    │   Container Start    │
    │  • Run entrypoint.sh │
    │  • Start Appium srvr │
    │  • Wait for health   │
    └──────────┬───────────┘
               │
               ▼
    ┌──────────────────────┐
    │   Running State      │
    │  • Accept requests   │
    │  • Execute tests     │
    │  • Log output        │
    └──────────┬───────────┘
               │
    ├─ (docker-compose stop)
    │
    ▼
    ┌──────────────────────┐
    │   Container Stop     │
    │  • Graceful shutdown │
    │  • Resource cleanup  │
    └──────────┬───────────┘
               │
    ├─ (docker-compose down)
    │
    ▼
    ┌──────────────────────┐
    │   Container Remove   │
    │  • Stop running      │
    │  • Remove container  │
    │  • (optional) volumes│
    └──────────────────────┘

┌─────────────────────────────────────┐
│   (docker-compose down -v)          │
│   Also removes volumes              │
└─────────────────────────────────────┘
```

## Network Communication Diagram

```
┌────────────────────────────────────────────────────────┐
│              Docker Bridge Network                      │
│           appium-network (172.20.0.0/16)               │
│                                                        │
│  ┌─────────────────────────┐  ┌──────────────────┐   │
│  │  Appium Container       │  │ Emulator         │   │
│  │  IP: 172.20.0.2         │  │ IP: 172.20.0.3   │   │
│  │  Hostname: appium-server│  │ Hostname: android│   │
│  │  Port: 4723             │  │ Port: 5554, 5555 │   │
│  └─────────────────────────┘  └──────────────────┘   │
│          ▲    │                       ▲               │
│          │    └───────────┬───────────┘               │
│          │                │                           │
│          └─── Built-in DNS resolution ────────────┐  │
│                                                    │  │
└────────────────────────────────────────────────────┘  │
         ▲                                               │
         │ Port mapping                                 │
         │ (container → host)                           │
         │                                              │
         ▼                                              │
    ┌─────────────────────────────────────────────────┐
    │         Host Machine Ports                       │
    │  Localhost (127.0.0.1)                          │
    │  • 4723    → appium-server:4723                 │
    │  • 5554-55 → emulator:5554-5555 (if enabled)   │
    │  • 80, 443 → nginx:80, 443 (if using proxy)    │
    └─────────────────────────────────────────────────┘
```

## Volume Mount Strategy

```
┌─────────────────────────────────────────┐
│        Container Filesystem             │
├─────────────────────────────────────────┤
│                                         │
│  /workspace                             │
│  ├── /src (← Host: ./src) [RO*]         │
│  │   ├── main/java/...                  │
│  │   └── test/java/...                  │
│  ├── /app (← Host: ./app) [RO*]         │
│  │   └── *.apk files                    │
│  ├── /target (← Host: ./target) [RW]    │
│  │   ├── classes/                       │
│  │   ├── test-classes/                  │
│  │   └── surefire-reports/              │
│  ├── /logs (← Host: ./logs) [RW]        │
│  │   └── appium.log                     │
│  └── pom.xml, Maven settings, etc.      │
│                                         │
│  /opt/appium (Named volume)             │
│  ├── node_modules/                      │
│  └── Appium dependencies                │
│                                         │
│  /opt/android-sdk                       │
│  ├── platform-tools/                    │
│  ├── emulator/                          │
│  └── ...                                │
│                                         │
│  /usr/lib/jvm/java-17-...               │
│  └── Java 17 runtime (no mount)         │
│                                         │
│  /var/log/nginx/ (if using proxy)       │
│  └── access.log, error.log              │
│                                         │
└─────────────────────────────────────────┘
   *RO = Read-only in production
    RW = Read-write (for output/artifacts)
```

---

**Complete Docker architecture designed for development, testing, and production deployment.**
