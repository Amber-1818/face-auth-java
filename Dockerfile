# ---------- BUILD STAGE ----------
FROM maven:3.8.8-openjdk-17 AS build

WORKDIR /app

# copy only maven config first for better cache
COPY pom.xml .
# if you have .mvn and mvnw include them too for wrapper-based builds
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw || true

# copy source
COPY src ./src

# build the app (skip tests for faster builds)
RUN mvn -B clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Recommended environment variables (Render provides PORT)
# Use server.port to let Spring bind to Render's PORT
ENV JAVA_OPTS=""
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

EXPOSE 8080

# Use Render's PORT env var (fall back to 8080 locally)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
