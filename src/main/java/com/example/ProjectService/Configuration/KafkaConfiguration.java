package com.example.ProjectService.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaConfiguration
{
    @Value("${spring.kafka.producer.topic.name}")
    private String topicName;

    // spring bean for kafka topic
    @Bean
    public NewTopic topic()
    {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
