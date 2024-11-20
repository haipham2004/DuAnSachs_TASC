FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/books-service-0.0.1-SNAPSHOT.jar books-service.jar

EXPOSE 8802

ENTRYPOINT ["java", "-jar", "/app/books-service.jar"]
