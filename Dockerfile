# Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build -x test

# Package stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/VoQal-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]