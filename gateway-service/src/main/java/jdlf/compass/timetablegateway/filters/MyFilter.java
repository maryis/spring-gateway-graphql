package jdlf.compass.timetablegateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MyFilter extends ModifyRequestBodyGatewayFilterFactory {
    @Override
    public GatewayFilter apply(Config config) {
        config.setRewriteFunction(String.class, String.class,
                (exchange, originContent) -> {
                    log.error("sdfsfsf:",originContent);
                    return Mono.just(originContent);
                });
        return super.apply(config);
    }

}