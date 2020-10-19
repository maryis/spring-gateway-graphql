package jdlf.compass.timetablegateway.filters;


import jdlf.compass.timetablegateway.exception.AuthenticationException;
import jdlf.compass.timetablegateway.exception.BadCredentialsException;
import jdlf.compass.timetablegateway.properties.SecurityAuthProperties;
import jdlf.compass.timetablegateway.security.basic.BasicAuthCredentials;
import jdlf.compass.timetablegateway.util.Base64Encoder;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


//authenticating user which called gateway
@Component
@Slf4j
public class AuthenticateRequestGlobalFilter1 implements GlobalFilter, Ordered {

    public static final String AUTH_CREDENTIALS = "AUTH_CREDENTIALS";
    public static final String PREFIX_BASIC = "Basic ";


    private SecurityAuthProperties properties;
    private Base64Encoder encoder;


    @Autowired
    public AuthenticateRequestGlobalFilter1(SecurityAuthProperties properties, Base64Encoder encoder) {
        this.properties = properties;
        this.encoder=encoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationGlobalFilter - start");

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {
            SecurityAuthProperties.Route routeSecurity = properties.getRoutes().get(route.getId());

            String inMemUser = routeSecurity.getUser();
            String inMemPass = routeSecurity.getPassword();

            BasicAuthCredentials credentials=extract(exchange.getRequest());
            if(!credentials.getUser().equals(inMemUser) || !credentials.getPassword().equals(inMemPass))
                throw new AuthenticationException("Authentication failed");

            exchange.getAttributes().put(AUTH_CREDENTIALS, credentials);
        }

        log.info("AuthenticationGlobalFilter - end");
        log.info(exchange.getRequest().getURI().getPath());

        return chain.filter(exchange);
    }

    public BasicAuthCredentials extract(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (headers == null || headers.isEmpty()) {
            throw new BadCredentialsException("Authorization header is missing!");
        }

        String authorization = headers.get(0);

        if (StringUtils.isEmpty(authorization)) {
            throw new BadCredentialsException("No Credentials Provided");
        }

        if (!authorization.startsWith(PREFIX_BASIC)) {
            throw new BadCredentialsException("Invalid Authorization header provided");
        }

        String[] authSegments = encoder.decode(authorization.substring(PREFIX_BASIC.length())).split(":");

        BasicAuthCredentials credentials = new BasicAuthCredentials();
        credentials.setUser(authSegments[0]);
        credentials.setPassword(authSegments[1]);

        return credentials;
    }


    @Override
    public int getOrder() {
        return 1;
    }

}
