FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar

EXPOSE 8804

ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]


## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim as build
#
#WORKDIR /app
##
#COPY ./core ./core
#WORKDIR /app/core
#RUN mvn clean install -DskipTests
#
#COPY ./payment-service ./payment-service
#WORKDIR /app/payment-service
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar
#
#EXPOSE 8804
#
#ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]
