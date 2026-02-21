# Multi-stage build for efficiency
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# copy only pom first to leverage Docker layer cache for dependencies
COPY pom.xml mvnw .mvn/ ./
RUN mvn -B -ntp -q dependency:go-offline

# copy full project and build
COPY src ./src
RUN mvn -B -ntp package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Install curl for healthcheck
RUN apt-get update && apt-get install -y --no-install-recommends curl ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# Expose port
EXPOSE 8080

# Environment variables (can be overridden by Render)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
CMD ["java", "-jar", "app.jar"]
