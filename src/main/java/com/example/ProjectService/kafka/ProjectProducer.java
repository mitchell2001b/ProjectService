package com.example.ProjectService.kafka;

import com.example.ProjectService.Events.ProjectCreatedEvent;
import com.example.ProjectService.Project.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class ProjectProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectProducer.class);
    private NewTopic topic;
    private KafkaTemplate<String, ProjectCreatedEvent> kafkaTemplate;
    private ObjectMapper ObjectMapperJson;

    public ProjectProducer(NewTopic topic, KafkaTemplate<String, ProjectCreatedEvent> kafkaTemplate, ObjectMapper objectMapper)
    {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.ObjectMapperJson = objectMapper;
    }

    public void SendMessage(Object projectCreatedEvent)
    {
        try {
            String jsonPayload = ObjectMapperJson.writeValueAsString(projectCreatedEvent);

            LOGGER.info(String.format("Project created event => %s", jsonPayload));

            Message<String> message = MessageBuilder
                    .withPayload(jsonPayload)
                    .setHeader(KafkaHeaders.TOPIC, topic.name())
                    .build();
            kafkaTemplate.send(message);

        } catch (JsonProcessingException e) {

            LOGGER.error("Error serializing payload to JSON", e);
        }
    }

}
