package com.playtomic.tests.wallet.configuration;

import com.playtomic.tests.wallet.service.StripeRestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StripeConfiguration {
    @Bean
    public RestTemplate stripeRestTemplate(
            final RestTemplateBuilder builder,
            final StripeRestTemplateResponseErrorHandler errorHandler
            ) {
        return builder
                .errorHandler(errorHandler)
                .build();
    }
}
