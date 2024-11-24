# Sử dụng OpenJDK làm base image, chọn phiên bản Java phù hợp với dự án của bạn
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép tệp JAR đã được build vào container (sau khi build dự án, tệp JAR sẽ ở trong thư mục target)
COPY target/orders-service-0.0.1-SNAPSHOT.jar orders-service.jar

# Mở cổng mà ứng dụng sẽ chạy trên container
EXPOSE 8803

# Chạy ứng dụng Spring Boot (tệp JAR) khi container được khởi động
ENTRYPOINT ["java", "-jar", "/app/orders-service.jar"]

## Giai đoạn build
#FROM maven:3.8.5-openjdk-17-slim AS build
#
#WORKDIR /app
#
#COPY ./core ./core
#COPY ./orders-service ./orders-service
#
#WORKDIR /app/core
#RUN mvn clean install -DskipTests
#
#WORKDIR /app/orders-service
#RUN mvn clean package -DskipTests
#
## Giai đoạn runtime
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/orders-service/target/orders-service-0.0.1-SNAPSHOT.jar orders-service.jar
#
#EXPOSE 8803
#
#ENTRYPOINT ["java", "-jar", "/app/orders-service.jar"]
