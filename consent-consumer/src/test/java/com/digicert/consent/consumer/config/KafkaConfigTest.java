package com.digicert.consent.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaConfigTest {

    @InjectMocks
    private KafkaConfig kafkaConfig;


    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress = "localhost:9092";
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId = "test";


    @Test
    public void kafkaConfigTest() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(kafkaConfig, "bootstrapAddress", bootstrapAddress);
        ReflectionTestUtils.setField(kafkaConfig, "groupId", groupId);

        ConsumerFactory<String, String> consumerFactory = kafkaConfig.consumerFactory();

        Map<String, Object> config = ((DefaultKafkaConsumerFactory<String, String>) consumerFactory).getConfigurationProperties();

        assertEquals(bootstrapAddress, config.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(groupId, config.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(StringDeserializer.class, config.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(StringDeserializer.class, config.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
    }
}
