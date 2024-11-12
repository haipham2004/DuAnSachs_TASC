package com.example.api_gateway_service.filter;

import com.example.api_gateway_service.dto.response.ApiResponse;
import com.example.api_gateway_service.dto.response.LoginResponse;
import com.example.api_gateway_service.service.UsersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered, GatewayFilter {
    @Lazy UsersService usersService;

    String[] publicEndpoints = {
            "/api/auth/public/fetchAccount",
            "/api/auth/public/checkToKen",
            "/api/auth/public/signin"
    };

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Kiểm tra xem endpoint có phải là public không
        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }
        // Lấy token từ header Authorization
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthorizedResponse(exchange.getResponse()); // Nếu không có token, trả về 401
        }

        String token = authHeader.get(0).replace("Bearer ", "");

        Mono<ApiResponse<LoginResponse>> result = usersService.validateToken(token);

        return result.flatMap(apiResponse -> {
            if (apiResponse != null && apiResponse.getResults().getUsername() != null) {
                return chain.filter(exchange); // Nếu token hợp lệ, tiếp tục chuỗi filter
            } else {
                return unauthorizedResponse(exchange.getResponse()); // Nếu không hợp lệ, trả về 401

            }
        }).onErrorResume(throwable -> {
            log.error("Error during token validation: {}", throwable.getMessage());
            return unauthorizedResponse(exchange.getResponse());
        });
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints).anyMatch(s ->
                request.getURI().getPath().matches(s));
    }

    private Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Filter có thứ tự thấp nhất, thực thi trước các filter khác
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
