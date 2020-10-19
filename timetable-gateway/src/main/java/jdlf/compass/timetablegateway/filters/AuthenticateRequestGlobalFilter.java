package com.compass.timetable.gateway.filters;


import com.compass.timetable.gateway.exception.AuthenticationException;
import com.compass.timetable.gateway.exception.BadCredentialsException;
import com.compass.timetable.gateway.properties.SecurityAuthProperties;
import com.compass.timetable.gateway.security.Authenticator;
import com.compass.timetable.gateway.security.basic.BasicAuthCredentials;
import com.compass.timetable.gateway.security.basic.BasicAuthenticator;
import com.compass.timetable.gateway.security.provider.AuthProvider;
import com.compass.timetable.gateway.security.provider.FileAuthProvider;
import com.compass.timetable.gateway.util.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


//authenticating user which called gateway
@Component
@Slf4j
public class AuthenticateRequestGlobalFilter implements GlobalFilter, Ordered {

    private SecurityAuthProperties properties;
    private Authenticator authenticator;

    @Autowired
    public AuthenticateRequestGlobalFilter(
            SecurityAuthProperties properties,
            @Qualifier("basicAuthenticator")Authenticator authenticator) {
        this.properties = properties;
        this.authenticator=authenticator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationGlobalFilter - start");

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {

            String serviceId=route.getId();
            boolean isValidUser=authenticator.authenticate(exchange.getRequest(),serviceId);

            if(isValidUser)
                return chain.filter(exchange);

            throw new BadCredentialsException("User Info does not found");
            //exchange.getAttributes().put(AUTH_CREDENTIALS, credentials);
        }

        throw new AuthenticationException("????????????? ");
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
