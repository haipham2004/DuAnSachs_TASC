FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/notifications-service-0.0.1-SNAPSHOT.jar notifications.jar

EXPOSE 2222

ENTRYPOINT ["java", "-jar", "/app/notifications.jar"]
## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim AS build
#
#WORKDIR /app
#
## Sao chép và build core (nếu core là một module Maven độc lập)
#COPY ./core ./core
#WORKDIR /app/core
#RUN mvn clean install -DskipTests
#
#
#COPY ./notifications-service ./notifications-service
#WORKDIR /app/notifications-service
#RUN mvn clean package -DskipTests
#
## Giai đoạn chạy
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
## Sao chép JAR đã build từ giai đoạn build
#COPY --from=build /app/notifications-service/target/notifications-service-0.0.1-SNAPSHOT.jar notifications-service.jar
#
## Mở cổng 8080 (hoặc cổng mà bạn muốn ứng dụng chạy)
#EXPOSE 8080
#
## Khởi chạy ứng dụng Spring Boot
#ENTRYPOINT ["java", "-jar", "/app/notifications-service.jar"]
