package com.atguigu.springcloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import sun.rmi.runtime.Log;

import java.util.function.Function;
import java.util.function.Predicate;

@Configuration
@Slf4j
public class GatewayConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("routeId1", new Function<PredicateSpec, Route.AsyncBuilder>() {
                    @Override
                    public Route.AsyncBuilder apply(PredicateSpec p) {
                        return p
                                .path("/guonei")
                                .uri("http://news.baidu.com/guonei")
                                .predicate(new Predicate<ServerWebExchange>() {
                                    @Override
                                    public boolean test(ServerWebExchange serverWebExchange) {
                                        log.info("serverWebExchange.getAttributes():{}", serverWebExchange.getAttributes());
                                        return true;
                                    }
                                });
                    }
                }).route("routeId2", new Function<PredicateSpec, Route.AsyncBuilder>() {
                    @Override
                    public Route.AsyncBuilder apply(PredicateSpec p) {
                        return p
                                .path("/guoji")
                                .uri("http://news.baidu.com/guoji")
                                .predicate(new Predicate<ServerWebExchange>() {
                                    @Override
                                    public boolean test(ServerWebExchange serverWebExchange) {
                                        log.info("serverWebExchange.getAttributes():{}", serverWebExchange.getAttributes());
                                        return true;
                                    }
                                });
                    }
                })
//                .route("routeId3", new Function<PredicateSpec, Route.AsyncBuilder>() {
//                    @Override
//                    public Route.AsyncBuilder apply(PredicateSpec p) {
//                        return p
//                                .path("/payment/**")
//                                .uri("lb://CLOUD-PAYMENT-SERVICE");
//                    }
//                })
                .build();
    }
}
