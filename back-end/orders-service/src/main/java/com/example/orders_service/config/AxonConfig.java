package com.example.orders_service.config;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Registration;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Configuration
public class AxonConfig {

    @Bean
    public CommandGateway commandGateway() {
        return new CommandGateway() {
            @Override
            public Registration registerDispatchInterceptor(MessageDispatchInterceptor<? super CommandMessage<?>> messageDispatchInterceptor) {
                return null;
            }

            @Override
            public <C, R> void send(C c, CommandCallback<? super C, ? super R> commandCallback) {

            }

            @Override
            public <R> R sendAndWait(Object o) {
                return null;
            }

            @Override
            public <R> R sendAndWait(Object o, long l, TimeUnit timeUnit) {
                return null;
            }

            @Override
            public <R> CompletableFuture<R> send(Object o) {
                return null;
            }
        };
    }
}