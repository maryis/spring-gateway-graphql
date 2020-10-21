package jdlf.compass.timetablegateway.filters;


import jdlf.compass.timetablegateway.exception.AuthenticationException;
import jdlf.compass.timetablegateway.exception.BadCredentialsException;
import jdlf.compass.timetablegateway.properties.SecurityAuthProperties;
import jdlf.compass.timetablegateway.security.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


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

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {

            String serviceId=route.getId();
            boolean isValidUser=authenticator.authenticate(exchange.getRequest(),serviceId);

            if(isValidUser)
                return chain.filter(exchange);

            log.error("User Info does not found");
            throw new BadCredentialsException("User Info does not found");
        }

        throw new AuthenticationException("????????????? ");
    }

    @Override
    public int getOrder() {
        return 3;
    }

}
