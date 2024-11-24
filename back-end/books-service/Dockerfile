FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/books-service-0.0.1-SNAPSHOT.jar books-service.jar

EXPOSE 8802

ENTRYPOINT ["java", "-jar", "/app/books-service.jar"]
## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim AS build
#
#WORKDIR /app
#
#COPY ./core ./core
#COPY ./books-service ./books-service
#
#WORKDIR /app/core
#RUN mvn clean install -DskipTests
#
#WORKDIR /app/books-service
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/books-service/target/books-service-0.0.1-SNAPSHOT.jar books-service.jar
#
#EXPOSE 8802
#
#ENTRYPOINT ["java", "-jar", "/app/books-service.jar"]
