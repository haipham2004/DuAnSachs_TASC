FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway-service.jar

EXPOSE 8763

ENTRYPOINT ["java", "-jar", "/app/api-gateway-service.jar"]

## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim as build
#
#WORKDIR /app
#
#COPY ./api-gateway-service ./api-gateway-service
#
#WORKDIR /app/api-gateway-service
#
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/api-gateway-service/target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway-service.jar
#
#EXPOSE 8763
#
#ENTRYPOINT ["java", "-jar", "/app/api-gateway-service.jar"]


#
## Sử dụng Maven + JDK 17 làm base image để build ứng dụng
#FROM maven:3.8.6-openjdk-17-slim AS build
#
## Sao chép mã nguồn vào container
#WORKDIR /app
#COPY . .
#
## Build ứng dụng Spring Boot (tạo file JAR)
#RUN mvn clean package -DskipTests
#
## Tạo một image riêng với OpenJDK để chạy ứng dụng
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
## Sao chép file JAR đã build từ container build vào container chạy
#COPY --from=build /app/target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway-service.jar
#
## Expose port 8763 (port mà ứng dụng của bạn sẽ chạy)
#EXPOSE 8763
#
## Lệnh để chạy ứng dụng
#ENTRYPOINT ["java", "-jar", "/app/api-gateway-service.jar"]
