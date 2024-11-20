FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar

EXPOSE 8802

ENTRYPOINT ["java", "-jar", "/app/eureka-service.jar"]
