package com.example.ProjectService.kafka;

import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.repositories.IAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class RegistrationConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationConsumer.class);
    private DateTimeFormatter DateTimeFormatterEvent = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ObjectMapper objectMapper;

    private IAccountRepository accountRepo;

    @Autowired
    public RegistrationConsumer(ObjectMapper givenObjectMapper, IAccountRepository accountRepository)
    {
        this.objectMapper = givenObjectMapper;
        this.accountRepo = accountRepository;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String jsonEvent) {
        try
        {
            Map<String, Object> eventMap = objectMapper.readValue(jsonEvent, new TypeReference<Map<String, Object>>() {
            });

            Number accountIdNumber = (Number) eventMap.get("id");
            Long accountId = accountIdNumber.longValue();
            String email = (String) eventMap.get("email");
            ProjectMember projectMember = new ProjectMember(accountId, email);
            if(!accountRepo.findById(accountId).isPresent())
            {
                this.accountRepo.save(projectMember);
            }

            LOGGER.info(String.format("User created event received in project service => %s", projectMember.toString()));
        }
        catch(JsonProcessingException e)
        {
            LOGGER.error("Error deserializing JSON message", e);
        }
    }

}
