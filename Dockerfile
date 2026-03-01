# Multi-stage build for efficiency
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# copy only pom first to leverage Docker layer cache for dependencies
# No need to copy the Maven wrapper when using the official Maven image
COPY pom.xml ./
RUN mvn -B -ntp -q dependency:go-offline

# copy full project and build
COPY src ./src
RUN mvn -B -ntp package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# 1. Python3 と pip のインストールを追加
RUN apt-get update && apt-get install -y --no-install-recommends \
    python3 \
    python3-pip \
    curl \
    ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# 2. Pythonスクリプト一式をコピー (scriptsフォルダが必要なため)
COPY scripts/ ./scripts/

# 3. もし Python で外部ライブラリ (requestsなど) を使う場合は以下も実行
# COPY scripts/requirements.txt .
# RUN pip3 install --no-cache-dir -r requirements.txt

# Ensure persistent data directory exists (Render provides /var/data)
RUN mkdir -p /var/data && chmod 777 /var/data

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
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
CMD ["java", "-jar", "app.jar"]
