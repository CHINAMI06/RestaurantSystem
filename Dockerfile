# Multi-stage build for efficiency
FROM amazoncorretto:17-al2-jdk as builder

WORKDIR /build

COPY . .

RUN yum install -y tar gzip && \
    curl -fsSL https://archive.apache.org -o apache-maven-3.9.6-bin.tar.gz && \
    tar xzf apache-maven-3.9.6-bin.tar.gz && \
    mv apache-maven-3.9.6 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn

RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-al2-jdk

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/*.jar app.jar

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
