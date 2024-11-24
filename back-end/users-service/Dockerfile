FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/users-service-0.0.1-SNAPSHOT.jar users-service.jar

EXPOSE 8801

ENTRYPOINT ["java", "-jar", "/app/users-service.jar"]

## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim AS build
#
#WORKDIR /app
#
#COPY ./users-service ./users-service
#
#WORKDIR /app/users-service
#
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/users-service/target/users-service-0.0.1-SNAPSHOT.jar users-service.jar
#
#EXPOSE 8801
#
#ENTRYPOINT ["java", "-jar", "/app/users-service.jar"]
