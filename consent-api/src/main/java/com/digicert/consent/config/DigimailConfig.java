package com.digicert.consent.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "digimail")
@Getter
@Setter
public class DigimailConfig {

    private String baseUrl;
    private String path;

}
