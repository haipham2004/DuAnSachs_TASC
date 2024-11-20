FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/core-0.0.1-SNAPSHOT.jar core.jar

EXPOSE 8805

ENTRYPOINT ["java", "-jar", "/app/core.jar"]
