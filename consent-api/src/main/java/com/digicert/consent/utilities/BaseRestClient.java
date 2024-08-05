package com.digicert.consent.utilities;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.UriTemplateRequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class BaseRestClient {

    private final RestTemplate restTemplate;
    private final String url;
    private final String path;

    protected <T> ResponseEntity<T> exchangeWithTimeLogging(RequestEntity<?> request, Class<T> responseType) {
        return doExchange(request, r -> restTemplate.exchange(request, responseType));
    }

    protected <T> ResponseEntity<T> exchangeWithTimeLogging(RestTemplate customRestTemplate,
                                                            RequestEntity<?> request,
                                                            Class<T> responseType) {
        return doExchange(request, r -> customRestTemplate.exchange(request, responseType));
    }

    protected String getUrl(String resource) {
        return url + path + resource;
    }

    @SneakyThrows({URISyntaxException.class, MalformedURLException.class})
    protected URI mkUri(String url) {
        return new URL(url).toURI();
    }

    private <T> ResponseEntity<T> doExchange(RequestEntity<?> request,
                                             Function<RequestEntity<?>, ResponseEntity<T>> function) {
        var startTime = System.currentTimeMillis();
        try {
            log.debug("Starting HTTP exchange: {} {}", request.getMethod(), defineUrl(request));

            var response = function.apply(request);

            log.info("Successfully completed HTTP exchange: {} {}. Status: {}",
                request.getMethod(),
                defineUrl(request),
                response.getStatusCode());

            return response;
        } finally {
            log.info("HTTP exchange {} {} took time: {} [msec], ", request.getMethod(), defineUrl(request),
                System.currentTimeMillis() - startTime);
        }
    }

    private static String defineUrl(RequestEntity<?> request) {
        if (request instanceof UriTemplateRequestEntity<?> uriTemplateRequest) {
            return uriTemplateRequest.getUriTemplate();
        }
        return request.getUrl().toString();
    }
}
