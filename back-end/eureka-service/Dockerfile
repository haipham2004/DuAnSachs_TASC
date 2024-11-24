FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar

EXPOSE 8802

ENTRYPOINT ["java", "-jar", "/app/eureka-service.jar"]
## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim AS build
#
#WORKDIR /app
#
#COPY ./eureka-service ./eureka-service
#
#WORKDIR /app/eureka-service
#
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/eureka-service/target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar
#
#EXPOSE 8802
#
#ENTRYPOINT ["java", "-jar", "/app/eureka-service.jar"]
