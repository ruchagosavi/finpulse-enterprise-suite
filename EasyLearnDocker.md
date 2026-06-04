# Docker Notes

Simple notes to learn Docker with Spring Boot Microservices.

---

# What is Docker?

Docker helps us package application + dependencies together and run anywhere.

Docker runs applications inside containers.

---

# Basic Terms

## Image

Blueprint/template of application.

## Container

Running instance of image.

## Dockerfile

File used to create Docker image.

## Docker Compose

Used to run multiple containers together.

---

# Install Docker

Install Docker Desktop.

Check installation:

```bash
docker --version
docker compose version
```

Login to Docker Hub:

```bash
docker login
```

---

# Important Note

Run all commands from microservice folder where `pom.xml` is present.

---

# Ways to Create Docker Images

1. Dockerfile
2. Buildpacks
3. Google Jib

---

# 1) Dockerfile Approach

## Step 1

Delete old compiled files from:

```text
target/
```

---

## Step 2

Add in `pom.xml`:

```xml
<packaging>jar</packaging>
```

---

## Step 3

Build project:

```bash
mvn clean install
```

This creates fat jar inside `target`.

Example:

```text
target/accounts-0.0.1-SNAPSHOT.jar
```

---

## Step 4

Run application:

```bash
mvn spring-boot:run
```

OR

```bash
java -jar target/accounts-0.0.1-SNAPSHOT.jar
```

---

# Create Dockerfile

Create file:

```text
Dockerfile
```

Example:

```dockerfile
FROM openjdk:17-jdk-slim

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts.jar

ENTRYPOINT ["java","-jar","accounts.jar"]
```

---

# Build Docker Image

```bash
docker build -t username/accounts:s4 .
```

Check images:

```bash
docker images
```

---

# Run Container

```bash
docker run -p 8080:8080 username/accounts:s4
```

Detached mode:

```bash
docker run -d -p 8080:8080 username/accounts:s4
```

---

# Important Docker Commands

## Running containers

```bash
docker ps
```

## All containers

```bash
docker ps -a
```

## Stop container

```bash
docker stop <container-id>
```

## Remove container

```bash
docker rm <container-id>
```

## Remove image

```bash
docker rmi <image-id>
```

## Logs

```bash
docker logs <container-id>
```

## Go inside container

```bash
docker exec -it <container-id> bash
```

---

# Port Mapping

```bash
-p localPort:containerPort
```

Example:

```bash
-p 8081:8080
```

Application runs on:

```text
localhost:8081
```

---

# Docker Compose

Used to run multiple microservices together.

Create:

```text
docker-compose.yml
```

Example:

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
```

Run:

```bash
docker compose up
```

Detached mode:

```bash
docker compose up -d
```

Stop:

```bash
docker compose down
```

---

# 2) Buildpacks

No Dockerfile needed.

Add inside Spring Boot Maven Plugin:

```xml
<image>
   <name>username/${project.artifactId}:s4</name>
</image>
```

Run:

```bash
mvn spring-boot:build-image
```

---

# 3) Google Jib

Build Docker image directly from Maven.

```bash
mvn compile jib:dockerBuild
```

Without Docker installed:

```bash
mvn compile jib:build
```

---

# Which Approach Should I Use?

## Dockerfile

When you need more control.

## Buildpacks

Fast and simple.

## Jib

Best for Java projects and CI/CD.

---

# Volumes

Used for persistent data.

Create volume:

```bash
docker volume create mysql-data
```

Mount volume:

```bash
docker run -v mysql-data:/var/lib/mysql mysql
```

---

# Environment Variables

```bash
docker run -e SPRING_PROFILES_ACTIVE=prod accounts:s4
```

---

# Docker Network

Create network:

```bash
docker network create backend
```

List networks:

```bash
docker network ls
```

---

# Push Image to Docker Hub

```bash
docker push docker.io/username/accounts:s4
```

---

# Useful Cleanup Commands

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

# Difference Between Docker & VM

Docker:

* Lightweight
* Faster
* Shares OS

VM:

* Heavy
* Full OS needed

---

# Multi-stage Docker Build

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

---

# Common Workflow

```bash
mvn clean install

docker build -t accounts:s4 .

docker run -d -p 8080:8080 accounts:s4

docker push docker.io/username/accounts:s4
```

---

# What Next After Docker?

```text
Docker
→ Docker Compose
→ Kubernetes
→ Helm
→ OpenShift
```

---

# Final Notes

Docker is commonly used with:

* Spring Boot
* Microservices
* Kafka
* Kubernetes
* CI/CD pipelines

These notes are focused on practical learning while building backend systems.
