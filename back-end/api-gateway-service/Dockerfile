FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway-service.jar

EXPOSE 8763

ENTRYPOINT ["java", "-jar", "/app/api-gateway-service.jar"]
