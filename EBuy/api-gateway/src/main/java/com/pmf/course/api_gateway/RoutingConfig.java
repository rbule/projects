package com.pmf.course.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, AuthenticationFilter authenticationFilter,UserIdInjectingGatewayFilter userIdFilter) {
        return builder.routes()
                .route("products-service-new", r -> r
                        .path("/products/new")
                        .filters(f -> f.filter(authenticationFilter).filter(userIdFilter))
                        .uri("http://products-service:8082")
                )
                .route("products-service", r -> r
                        .path("/products/**")
                        .uri("http://products-service:8082")
                )
                .route("order-service", r -> r
                        .path("/order/**")
                        .filters(f -> f.filter(authenticationFilter).filter(userIdFilter))
                        .uri("http://order-service:8081")
                )
                .build();
    }
}
