package jdlf.compass.timetablegateway.filters;

import io.netty.buffer.ByteBufAllocator;
import jdlf.compass.timetablegateway.configs.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class GatewayCacheRequestFilter
        implements GlobalFilter, Ordered {

    /**
     * default HttpMessageReader
     */
    private static final List<HttpMessageReader<?>> messageReaders =
            HandlerStrategies.withDefaults().messageReaders();

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        /**
         * save request path and serviceId into gateway context
         */

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setPath(path);
        /**
         * save gateway context into exchange
         */
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT,
                gatewayContext);
        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();

        if (request.getMethod() == HttpMethod.POST) {
            Mono<Void> voidMono = null;
            if (MediaType.APPLICATION_JSON.equals(contentType)
                    || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
                voidMono =
                        readBody(exchange, chain, gatewayContext);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                voidMono =
                        readFormData(exchange, chain, gatewayContext);
            }

            return voidMono;
        }
        /* log.debug(
            "[GatewayContext]ContentType:{},Gateway context is set with {}",
            contentType, gatewayContext);*/
        return chain.filter(exchange);

    }

    /**
     * ReadFormData
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> readFormData(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            GatewayContext gatewayContext) {
        final ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        return exchange.getFormData()
                .doOnNext(multiValueMap -> {
                    gatewayContext.setFormData(multiValueMap);
                })
                .then(Mono.defer(() -> {
                    Charset charset = headers.getContentType().getCharset();
                    charset = charset == null ? StandardCharsets.UTF_8 : charset;
                    String charsetName = charset.name();
                    MultiValueMap<String, String> formData =
                            gatewayContext.getFormData();
                    /**
                     * formData is empty just return
                     */
                    if (null == formData || formData.isEmpty()) {
                        return chain.filter(exchange);
                    }
                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    String entryKey;
                    List<String> entryValue;
                    try {
                        /**
                         * repackage form data
                         */
                        for (Map.Entry<String, List<String>> entry : formData
                                .entrySet()) {
                            entryKey = entry.getKey();
                            entryValue = entry.getValue();
                            if (entryValue.size() > 1) {
                                for (String value : entryValue) {
                                    formDataBodyBuilder.append(entryKey).append("=")
                                            .append(
                                                    URLEncoder.encode(value, charsetName))
                                            .append("&");
                                }
                            } else {
                                formDataBodyBuilder
                                        .append(entryKey).append("=").append(URLEncoder
                                        .encode(entryValue.get(0), charsetName))
                                        .append("&");
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        // ignore URLEncode Exception
                    }
                    /**
                     * substring with the last char '&'
                     */
                    String formDataBodyString = "";
                    if (formDataBodyBuilder.length() > 0) {
                        formDataBodyString = formDataBodyBuilder.substring(0,
                                formDataBodyBuilder.length() - 1);
                    }
                    /**
                     * get data bytes
                     */
                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    int contentLength = bodyBytes.length;
                    ServerHttpRequestDecorator decorator =
                            new ServerHttpRequestDecorator(
                                    request) {
                                /**
                                 * change content-length
                                 *
                                 * @return
                                 */
                                @Override
                                public HttpHeaders getHeaders() {
                                    HttpHeaders httpHeaders = new HttpHeaders();
                                    httpHeaders.putAll(super.getHeaders());
                                    if (contentLength > 0) {
                                        httpHeaders.setContentLength(contentLength);
                                    } else {
                                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING,
                                                "chunked");
                                    }
                                    return httpHeaders;
                                }

                                /**
                                 * read bytes to Flux<Databuffer>
                                 *
                                 * @return
                                 */
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return DataBufferUtils
                                            .read(new ByteArrayResource(bodyBytes),
                                                    new NettyDataBufferFactory(
                                                            ByteBufAllocator.DEFAULT),
                                                    contentLength);
                                }
                            };
                    ServerWebExchange mutateExchange =
                            exchange.mutate().request(decorator).build();
                /*   log.info("[GatewayContext]Rewrite Form Data :{}",
                       formDataBodyString);*/

                    return chain.filter(mutateExchange);
                }));
    }

    /**
     * ReadJsonBody
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> readBody(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            GatewayContext gatewayContext) {
        /**
         * join the body
         */
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    /*
                     * read the body Flux<DataBuffer>, and release the buffer
                     * //TODO when SpringCloudGateway Version Release To G.SR2,this can be update with the new version's feature
                     * see PR https://github.com/spring-cloud/spring-cloud-gateway/pull/1095
                     */
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                        DataBuffer buffer =
                                exchange.getResponse().bufferFactory().wrap(bytes);
                        DataBufferUtils.retain(buffer);
                        return Mono.just(buffer);
                    });
                    /**
                     * repackage ServerHttpRequest
                     */
                    ServerHttpRequest mutatedRequest =
                            new ServerHttpRequestDecorator(exchange.getRequest()) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return cachedFlux;
                                }
                            };
                    /**
                     * mutate exchage with new ServerHttpRequest
                     */
                    ServerWebExchange mutatedExchange =
                            exchange.mutate().request(mutatedRequest).build();
                    /**
                     * read body string with default messageReaders
                     */
                    return ServerRequest.create(mutatedExchange, messageReaders)
                            .bodyToMono(String.class)
                            .doOnNext(objectValue -> {
                                gatewayContext.setCacheBody(objectValue);

                            }).then(chain.filter(mutatedExchange));
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}