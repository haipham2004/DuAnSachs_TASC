//package com.example.api_gateway_service.config;
//
//import com.example.api_gateway_service.filter.AuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
//public class ApiGatewayConfig {
//
//
//    private AuthenticationFilter authenticationFilter;
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r
//                        .path("/users/**","/api/auth/public/**")
//                        .filters(f -> f.filter(authenticationFilter))
//                        .uri("lb://USERS-SERVICE")
//                )
//                .build();
//    }
//}
