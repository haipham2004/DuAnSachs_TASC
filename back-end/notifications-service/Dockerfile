FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/notifications-service-0.0.1-SNAPSHOT.jar notifications.jar

EXPOSE 2222

ENTRYPOINT ["java", "-jar", "/app/notifications.jar"]
