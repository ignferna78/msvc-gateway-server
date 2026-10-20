package com.springcloud.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

  private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("ejecutando filtro antes del request PRE");

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request -> request.headers(headers -> headers.add("token", "abcdef")))
                .build();

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(()->{

            logger.info("ejecutando filtro POST response");

            String token = mutatedExchange.getRequest().getHeaders().getFirst("token");
            logger.info("token:  " + token);

            mutatedExchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
            mutatedExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}

