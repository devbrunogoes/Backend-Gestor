FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first to speed up subsequent builds
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build the application
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV PORT=8080
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
