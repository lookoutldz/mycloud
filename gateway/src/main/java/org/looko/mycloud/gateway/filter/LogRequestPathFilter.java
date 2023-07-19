package org.looko.mycloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class LogRequestPathFilter implements WebFilter {
    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String authorization = exchange.getRequest().getHeaders().getFirst("authorization");
        log.info(method + "[" + path + "]" + authorization);
        return chain.filter(exchange);
    }
}
