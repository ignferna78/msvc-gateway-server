package com.springcloud.app.gateway.filters.factory;

import com.springcloud.app.gateway.filters.SampleGlobalFilter;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {


    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        return (exchange, chain) -> {

            logger.info("ejecutando pre gateway filterFactory: " + config.message);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                Optional.ofNullable(config.value).ifPresent(cookie -> {
                    logger.info("cookie:  " + cookie);
                    exchange.getResponse().addCookie(ResponseCookie.from(config.name, cookie).build());
                });
                logger.info("ejecutando POST gateway filterFactory: " + config.message);
            }));
        };

    }



    @Override
    public List<String> shortcutFieldOrder() {

        return Arrays.asList("message", "name", "value");
    }

    @Override
    public String name() {

        return "EjemploCookie";
    }


    @Data
    public static class ConfigurationCookie {

        private String name;
        private String value;
        private String message;


    }
}
