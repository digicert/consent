package com.digicert.consent.consumer.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Component
public class StatusEventConsumer {


    @KafkaListener(topics = "${spring.kafka.topics.ivm-status}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
       log.info("Consumed Identity provider status message: {}", message);
        saveStatusUpdate(message);
    }


    private void saveStatusUpdate(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> request = new HttpEntity<>(message, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange("http://127.0.0.1:8081/consent/identity-provider/status" , HttpMethod.POST, request, Boolean.class);
        response.getBody();
    }
}