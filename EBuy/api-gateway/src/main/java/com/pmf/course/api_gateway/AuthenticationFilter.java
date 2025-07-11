package com.pmf.course.api_gateway;

import com.pmf.course.api_gateway.exceptions.UnauthenticatedException;
import com.pmf.course.api_gateway.storage.ApiKeyRepository;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class AuthenticationFilter implements GatewayFilter, Ordered {

    private final ApiKeyRepository apiKeyRepository;

    public AuthenticationFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var authenticated = extractApiKey(exchange)
                .filter(apiKeyRepository::existsById)
                .isPresent();
        if (!authenticated) {
            return Mono.error(new UnauthenticatedException());
        }
        return chain.filter(exchange);
    }

    private Optional<String> extractApiKey(ServerWebExchange exchange) {
        return Optional.ofNullable(firstAuthorizationHeader(exchange))
                .filter(val -> val.startsWith("App "))
                .map(val -> val.substring(4).trim());
    }

    private String firstAuthorizationHeader(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

}
