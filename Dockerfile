# Multi-stage Dockerfile for Spring Boot Application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-25 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
# This allows Docker to cache dependencies if pom.xml hasn't changed
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
# The resulting JAR will be in /app/target/
RUN mvn clean package -DskipTests -B

# Stage 2: Create the runtime image
FROM eclipse-temurin:25-jre-alpine

# Set working directory
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the JAR from the build stage
# The JAR name is based on artifactId and version from pom.xml: SecureStorage-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/SecureStorage-*.jar app.jar

# Change ownership to the non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# Configure JVM options for containerized environment
# - Use container-aware memory settings
# - Enable JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check (optional but recommended)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

