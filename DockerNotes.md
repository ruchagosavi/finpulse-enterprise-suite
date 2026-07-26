# Docker Notes for Spring Boot Microservices 🚀

Comprehensive Docker notes focused on Java, Spring Boot, Microservices, Docker Compose, Buildpacks, and Google Jib.

These are practical developer notes created while learning containerization for backend systems and microservices architecture.

---

# Table of Contents

1. Why Docker?
2. Docker Architecture
3. Docker Installation & Login
4. Docker Core Concepts
5. Dockerfile Approach
6. Buildpacks Approach
7. Google Jib Approach
8. Docker Images vs Containers
9. Docker Networking
10. Docker Volumes
11. Environment Variables
12. Docker Compose
13. Multi-stage Docker Builds
14. Docker Commands Deep Dive
15. Docker Logs & Debugging
16. Docker Cleanup Commands
17. Docker Hub
18. Production Workflow
19. Docker Interview Revision
20. What to Learn Next

---

# Why Docker?

Docker helps package applications along with dependencies into lightweight containers.

Benefits:

* Consistent environments
* Faster deployments
* Isolation
* Scalability
* Lightweight compared to VMs
* Works across different systems

Docker is heavily used in:

* Microservices
* Cloud deployments
* Kubernetes
* CI/CD pipelines
* Fintech systems
* Enterprise backend applications

---

# Docker Architecture

Docker consists of:

* Docker Client
* Docker Daemon
* Docker Engine
* Docker Registry

Flow:

```text
Developer → Docker Client → Docker Daemon → Images/Containers
```

---

# Docker Installation & Login

## Install Docker Desktop

Official Website:

https://www.docker.com/products/docker-desktop/

---

## Verify Installation

```bash
docker --version
docker compose version
```

---

## Login to Docker Hub

```bash
docker login
```

---

# Docker Core Concepts

## Image

Blueprint/template used to create containers.

Example:

```text
accounts-service image
```

---

## Container

Running instance of an image.

Example:

```text
accounts-service container
```

---

## Dockerfile

Text file containing instructions to build Docker images.

---

## Docker Compose

Tool used to run multiple containers together.

---

# IMPORTANT NOTE

Always run commands from the respective microservice folder where `pom.xml` is present.

---

# 3 Approaches to Generate Docker Images

1. Dockerfile
2. Buildpacks
3. Google Jib

---

# 1) Dockerfile Approach

## Step 1: Clean Previous Build

Delete all compiled content inside:

```text
target/
```

---

## Step 2: Add Packaging

Inside `pom.xml`:

```xml
<packaging>jar</packaging>
```

---

## Step 3: Build Application

```bash
mvn clean install
```

This generates a fat JAR file inside `target/`.

Example:

```text
target/accounts-0.0.1-SNAPSHOT.jar
```

---

## Step 4: Verify Application

Using Maven:

```bash
mvn spring-boot:run
```

Using Java:

```bash
java -jar target/accounts-0.0.1-SNAPSHOT.jar
```

Using `java -jar` is preferred inside containers because Maven is not needed there.

---

# Creating Dockerfile

Create file:

```text
Dockerfile
```

Example:

```dockerfile
FROM openjdk:17-jdk-slim

LABEL maintainer="yourname"

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts.jar

ENTRYPOINT ["java","-jar","accounts.jar"]
```

---

# Build Docker Image

```bash
docker build -t dockerusername/accounts:s4 .
```

Example:

```bash
docker build -t rucha/accounts:s4 .
```

---

# Check Docker Images

```bash
docker images
```

Inspect image:

```bash
docker image inspect <image-id>
```

---

# Create Docker Container

```bash
docker run -p 8080:8080 dockerusername/accounts:s4
```

Where:

* First port → local machine port
* Second port → container port

---

# Detached Mode

Run container in background:

```bash
docker run -d -p 8080:8080 dockerusername/accounts:s4
```

---

# Port Mapping / Port Publishing

```bash
-p localPort:containerPort
```

Example:

```bash
-p 8081:8080
```

Application accessible at:

```text
http://localhost:8081
```

---

# Docker Networking

Containers communicate using Docker networks.

## Create Network

```bash
docker network create backend
```

## List Networks

```bash
docker network ls
```

Most microservices communicate through:

* bridge networks
* Docker Compose networks

---

# Docker Volumes

Volumes provide persistent storage.

Without volumes, data gets deleted after container removal.

## Create Volume

```bash
docker volume create mysql-data
```

## Mount Volume

```bash
docker run -v mysql-data:/var/lib/mysql mysql
```

Used for:

* Databases
* Logs
* Persistent storage

---

# Environment Variables

Used for:

* DB credentials
* Profiles
* Kafka configs
* Secrets
* API URLs

Example:

```bash
docker run -e SPRING_PROFILES_ACTIVE=prod accounts:s4
```

---

# CMD vs ENTRYPOINT

## CMD

Can be overridden.

## ENTRYPOINT

Fixed executable.

Spring Boot commonly uses:

```dockerfile
ENTRYPOINT ["java","-jar","app.jar"]
```

---

# EXPOSE vs -p

## EXPOSE

Documentation inside image.

Example:

```dockerfile
EXPOSE 8080
```

---

## -p

Actually publishes port to host machine.

Example:

```bash
-p 8080:8080
```

---

# Disadvantages of Dockerfile Approach

* More learning curve
* Manual maintenance
* More configuration

---

# 2) Buildpacks Approach

Buildpacks automatically create optimized images.

---

## Add Packaging

```xml
<packaging>jar</packaging>
```

---

## Configure Image Name

Inside Spring Boot Maven Plugin:

```xml
<image>
    <name>dockerusername/${project.artifactId}:s4</name>
</image>
```

---

## Generate Docker Image

```bash
mvn spring-boot:build-image
```

Advantages:

* No Dockerfile needed
* Smaller images
* Easier setup
* Faster development

Best for:

* Standard Spring Boot applications

---

# 3) Google Jib Approach

Jib builds Docker images directly from Maven/Gradle.

---

## Build Docker Image

```bash
mvn compile jib:dockerBuild
```

---

## Build Without Docker Installed

```bash
mvn compile jib:build
```

Useful for:

* CI/CD
* Cloud-native workflows
* Kubernetes deployments

---

# Which Approach Should Be Used?

## Dockerfile

Use when:

* Full control needed
* Custom Linux setup
* Multi-stage builds
* Production optimization

---

## Buildpacks

Use when:

* Fast development needed
* Standard Spring Boot apps
* Minimal Docker knowledge

---

## Google Jib

Use when:

* Java microservices
* CI/CD pipelines
* Kubernetes
* Faster builds required

---

# Multi-stage Docker Builds

Used to reduce image size.

Example:

```dockerfile
FROM maven:3.9 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/accounts.jar accounts.jar

ENTRYPOINT ["java","-jar","accounts.jar"]
```

Benefits:

* Smaller image size
* Better security
* Production-ready containers

---

# Push Docker Image to Docker Hub

```bash
docker push docker.io/dockerusername/accounts:s4
```

---

# Docker Compose

Docker Compose runs multiple services together.

Create:

```text
docker-compose.yml
```

---

# Example docker-compose.yml

```yaml
version: '3.8'

services:

  accounts:
    image: rucha/accounts:s4
    ports:
      - "8080:8080"

  loans:
    image: rucha/loans:s4
    ports:
      - "8090:8090"

networks:
  default:
    driver: bridge
```

---

# Docker Compose Commands

Start services:

```bash
docker compose up
```

Detached mode:

```bash
docker compose up -d
```

Stop services:

```bash
docker compose down
```

---

# Docker Commands Deep Dive

## Images

```bash
docker images
```

---

## Running Containers

```bash
docker ps
```

---

## All Containers

```bash
docker ps -a
```

---

## Stop Container

```bash
docker stop <container-id>
```

---

## Remove Container

```bash
docker rm <container-id>
```

---

## Remove Image

```bash
docker rmi <image-id>
```

---

## Inspect Image

```bash
docker image inspect <image-id>
```

---

## View Logs

```bash
docker logs <container-id>
```

---

## Execute Inside Container

```bash
docker exec -it <container-id> bash
```

---

## Container Resource Usage

```bash
docker stats
```

---

# Docker Logs & Debugging

Useful debugging commands:

```bash
docker logs <container-id>

docker exec -it <container-id> bash

docker inspect <container-id>

docker stats
```

---

# Docker Cleanup Commands

Remove stopped containers:

```bash
docker container prune
```

Remove unused images:

```bash
docker image prune
```

Remove everything unused:

```bash
docker system prune
```

---

# Real Production Workflow

Typical enterprise workflow:

```text
Developer
   ↓
GitHub
   ↓
CI/CD Pipeline
(Jenkins / GitHub Actions)
   ↓
Docker Build
   ↓
Docker Registry
   ↓
Kubernetes / OpenShift
```

---

# Kubernetes & OpenShift

Docker containers are usually orchestrated using:

* Kubernetes
* Red Hat OpenShift

These platforms handle:

* Scaling
* Load balancing
* Self-healing
* Rolling deployments

---

# VS Code Docker Extension

Useful features:

* Container explorer
* Logs viewer
* Image management
* Docker Compose integration

---

# Common Interview Questions

## Difference Between Image & Container

Image:

* Blueprint/template

Container:

* Running instance of image

---

## Difference Between VM & Docker

Docker:

* Lightweight
* Shares host OS kernel

VM:

* Heavy
* Full operating system

---

## Difference Between Dockerfile & Docker Compose

Dockerfile:

* Builds image

Docker Compose:

* Runs multiple containers together

---

## What is Port Mapping?

Connecting local machine port with container port.

Example:

```bash
-p 8080:8080
```

---

## What is Detached Mode?

Runs container in background.

```bash
docker run -d
```

---

# Most Common Docker Workflow

```bash
mvn clean install

docker build -t accounts:s4 .

docker run -d -p 8080:8080 accounts:s4

docker push docker.io/dockerusername/accounts:s4

docker compose up
```

---

# What To Learn Next

Recommended roadmap:

```text
Docker Basics
→ Docker Compose
→ Networking
→ Volumes
→ Multi-stage Builds
→ Kubernetes
→ Helm
→ OpenShift
→ CI/CD Pipelines
```

---

# Final Notes

These notes were created while learning Docker for:

* Java Backend Development
* Spring Boot Microservices
* Kafka-based systems
* Cloud-native applications
* Fintech backend engineering

The goal is practical understanding rather than only theoretical concepts.
