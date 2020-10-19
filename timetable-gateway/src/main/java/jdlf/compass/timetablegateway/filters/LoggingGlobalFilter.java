package jdlf.compass.timetablegateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
  Log log = LogFactory.getLog(getClass());

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    Set<URI> uris =
        exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
    String originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString();
    Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
    URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
    log.info(
        "Incoming request "
            + originalUri
            + " is routed to id: "
            + route.getId()
            + ", uri:"
            + routeUri);

    return chain
        .filter(exchange)
        .then(
            Mono.fromRunnable(
                () -> {
                  String responseHeaders =
                      exchange.getRequest().getHeaders().toSingleValueMap().toString();
                  int status = exchange.getResponse().getStatusCode().value();
                  log.info(
                      "Outgoing Response "
                          + originalUri
                          + " status : "
                          + exchange.getResponse().getStatusCode());
                }));
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
