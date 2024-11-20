FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/users-service-0.0.1-SNAPSHOT.jar users-service.jar

EXPOSE 8801

ENTRYPOINT ["java", "-jar", "/app/users-service.jar"]
