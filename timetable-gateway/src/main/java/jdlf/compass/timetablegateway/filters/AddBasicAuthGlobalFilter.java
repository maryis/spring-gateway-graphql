package com.compass.timetable.gateway.filters;


import com.compass.timetable.gateway.properties.SecurityAuthProperties;
import com.compass.timetable.gateway.util.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AddBasicAuthGlobalFilter implements GlobalFilter, Ordered {

    public static final String BASIC_AUTH_PREFIX = "Basic ";

    private SecurityAuthProperties properties;

    private Base64Encoder encoder;

    @Autowired
    public AddBasicAuthGlobalFilter(SecurityAuthProperties properties, Base64Encoder encoder) {
        this.properties = properties;
        this.encoder = encoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("BasicAuthRelayGlobalFilter - start");

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {
            SecurityAuthProperties.Route routeSecurity = properties.getRoutes().get(route.getId());

            if (routeSecurity.getUser() != null && routeSecurity.getPassword() != null) {
                String encodedAuth = BASIC_AUTH_PREFIX + encoder.encode(routeSecurity.getUser()
                                                                       + ":" + routeSecurity.getPassword());

                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .headers(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, encodedAuth))
                        .build();

                log.info("BasicAuthRelayGlobalFilter - has pass - end");
                log.info("route: "+route);
                return chain.filter(exchange.mutate().request(request).build());
            }
        }

        log.info("BasicAuthRelayGlobalFilter - without pass - end");
        log.info("route: "+route);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
