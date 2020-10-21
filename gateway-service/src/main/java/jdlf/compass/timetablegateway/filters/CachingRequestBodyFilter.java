//package jdlf.compass.timetablegateway.filters;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//class CachingRequestBodyFilter extends AbstractGatewayFilterFactory<CachingRequestBodyFilter.Config> implements Ordered{
//
//        public CachingRequestBodyFilter() {
//        super(Config.class);
//    }
//
//    public GatewayFilter apply(final Config config) {
//        return (exchange, chain) -> ServerWebExchangeUtils.cacheRequestBody(exchange,
//                (serverHttpRequest) -> chain.filter(exchange.mutate().request(serverHttpRequest).build()));
//    }
//
//    public static class Config {
//    }
//
//    @Override
//    public int getOrder() {
//        return 5;
//    }
//}