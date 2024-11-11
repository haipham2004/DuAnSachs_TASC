//package com.example.api_gateway_service.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ApiGatewayConfig {
//
//    @Autowired
//    private AuthenticationFilter authenticationFilter;
//
//    @Bean
//    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
//        return builder.routes()
//                // Route có yêu cầu xác thực JWT (bảo mật)
//                .route("secured-route", r -> r.path("/users/**")
//                        .or().path("/api/auth/public/**")
//                        .or().path("/testnhe/**")  // Các đường dẫn cần bảo vệ
//                        .filters(f -> f.filter(authenticationFilter))  // Áp dụng filter JWT cho các route này
//                        .uri("lb://USERS-SERVICE"))  // Định tuyến đến USERS-SERVICE qua load balancer
//
//                // Route công cộng không cần bảo mật
//                .route("public-route", r -> r.path("/api/auth/public/fetchAccount")  // Endpoint công cộng không cần bảo mật
//                        .uri("lb://USERS-SERVICE"))  // Định tuyến đến USERS-SERVICE
//
//                // Bạn có thể thêm các route khác vào đây nếu cần
//                .build();
//    }
//}