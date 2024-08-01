/*
package com.digicert.consent.consumer.consumers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StatusEventConsumerTest {

    @InjectMocks
    private StatusEventConsumer statusEventConsumer;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsume() {
        // Arrange
        String message = "{\"status\":\"active\",\"identId\":\"12345\",\"providerName\":\"provider\"}";
        ResponseEntity<Boolean> responseEntity = ResponseEntity.ok(true);

        when(restTemplate.exchange(
                eq("http://127.0.0.1:8081/consent/identity-provider/status"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Boolean.class)))
                .thenReturn(responseEntity);

        // Act
        statusEventConsumer.consume(message);

        // Assert
        verify(restTemplate, times(1)).exchange(
                eq("http://127.0.0.1:8081/consent/identity-provider/status"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Boolean.class));
    }
}
*/
