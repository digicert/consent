package com.digicert.consent.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class RestTemplateConfiguration {

    @Bean(name = "restTemplate")
    public RestTemplate getRestClient() {
        ClientHttpRequestInterceptor interceptor = (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
            request.getHeaders().set("accept", "application/json");
            request.getHeaders().set("X-Transaction-ID", UUID.randomUUID().toString());
            return execution.execute(request, body);
        };

        return new RestTemplateBuilder()
                .additionalInterceptors(interceptor)
                .build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
