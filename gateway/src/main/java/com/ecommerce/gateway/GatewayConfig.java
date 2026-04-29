package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(f -> f
                                .retry(retry -> retry
                                        .setRetries(1)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(500), 2, true)
                                )
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/products")))
                        .uri("lb://product-service"))
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .retry(retry -> retry
                                        .setRetries(1)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(500), 2, true)
                                )
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/users")))
                        .uri("lb://user-service"))
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/cart/**")
                        .filters(f -> f
                                .retry(retry -> retry
                                        .setRetries(1)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(500), 2, true)
                                )
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/orders")))
                        .uri("lb://order-service"))
                .route("eureka-server", r -> r
                        .path("/eureka/main")
                        .filters(f -> f
                                .rewritePath("/eureka/main", "/")
                        )
                        .uri("http://localhost:8761"))
                .route("eureka-server-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}