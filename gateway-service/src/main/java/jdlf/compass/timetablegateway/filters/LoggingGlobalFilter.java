package jdlf.compass.timetablegateway.filters;

import jdlf.compass.timetablegateway.configs.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info( "Incoming requestId={}, from origin={} ",exchange.getRequest().getId(), exchange.getRequest().getLocalAddress());

        long startTime = System.currentTimeMillis();
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);

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
                                    log.info("Response for requestQuery={}, ResponseStatus={}, ResponseTime(ms)={}",
                                            gatewayContext.getCacheBody(),
                                            serviceResponseStatus,
                                            execTime
                                            );

                                   }));

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
