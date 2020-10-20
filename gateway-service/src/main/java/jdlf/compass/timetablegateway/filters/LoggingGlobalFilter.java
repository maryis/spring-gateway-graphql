package jdlf.compass.timetablegateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Component
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Set<URI> uris =
                exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
        String originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        log.info( "Incoming request "+ route);

        return chain
                .filter(exchange)
                .then(
                        Mono.fromRunnable(
                                () -> {

                                    HttpStatus status = exchange.getResponse().getStatusCode();
                                    if (status != HttpStatus.OK)
                                        log.info("Outgoing Response Route "+ route );

                                    log.error(
                                                "Outgoing Response:  "
                                                        + exchange.getResponse().toString() );
                                }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
