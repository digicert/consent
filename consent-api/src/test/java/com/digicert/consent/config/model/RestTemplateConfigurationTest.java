package com.digicert.consent.config.model;

import com.digicert.consent.config.RestTemplateConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestTemplateConfigurationTest {

    @Test
    public void getRestClientTest() {

        // Arrange
        RestTemplateConfiguration restTemplateConfiguration = new RestTemplateConfiguration();

        // Act
        restTemplateConfiguration.getRestClient();

    }
}
