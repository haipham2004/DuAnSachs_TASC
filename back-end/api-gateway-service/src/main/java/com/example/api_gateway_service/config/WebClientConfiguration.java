package com.example.api_gateway_service.config;

import com.example.api_gateway_service.repository.UserRepositoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8801/api/auth/public/checkToKen")
                .build();
    }

    @Bean
    UserRepositoryClient identityClient(WebClient webClient) {

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(
                WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(UserRepositoryClient.class);
    }

}
