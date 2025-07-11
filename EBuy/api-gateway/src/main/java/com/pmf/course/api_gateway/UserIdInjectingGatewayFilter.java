package com.pmf.course.api_gateway;

import com.pmf.course.api_gateway.storage.ApiKeyRepository;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class UserIdInjectingGatewayFilter implements GatewayFilter, Ordered {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("App ")) {
            String apiKey = authHeader.substring(4).trim();
            Long userId = apiKeyRepository.findBySecret(apiKey).getUserId();

            if (userId != null) {
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(builder -> builder.header("X-User-Id", userId.toString()))
                        .build();

                return chain.filter(mutatedExchange);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
