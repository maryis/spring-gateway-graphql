package jdlf.compass.timetablegateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        log.info( "Incoming requestId={}, from origin={} ",exchange.getRequest().getId(), exchange.getRequest().getLocalAddress());

        long startTime = System.currentTimeMillis();

        return chain
                .filter(exchange)
                .then(
                        Mono.fromRunnable(
                                () -> {
                                    String serviceResponseStatus= exchange.getResponse().getStatusCode().toString();

                                    if(serviceResponseStatus.startsWith("2")){
                                        serviceResponseStatus="Success";
                                    }
                                    else{
                                        serviceResponseStatus="Failure";
                                    }



                                    long execTime = System.currentTimeMillis() - startTime;
                                    log.info("Response requestId={}, serviceResponseStatus={}, serviceResponseTime(ms)={}",
                                            exchange.getRequest().getId(),
                                            serviceResponseStatus,
                                            execTime
                                            );

                                   }));

    }

    @Override
    public int getOrder() {
        return 4;
    }
}
