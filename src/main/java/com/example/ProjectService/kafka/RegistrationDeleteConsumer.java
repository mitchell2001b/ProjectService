package com.example.ProjectService.kafka;

import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.repositories.IAccountRepository;
import com.example.ProjectService.repositories.IProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
public class RegistrationDeleteConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationConsumer.class);
    private DateTimeFormatter DateTimeFormatterEvent = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ObjectMapper objectMapper;

    private IProjectRepository projectRepo;
    private IAccountRepository accountRepo;

    @Autowired
    public RegistrationDeleteConsumer(ObjectMapper givenObjectMapper, IAccountRepository accountRepository, IProjectRepository projectRepository)
    {
        this.objectMapper = givenObjectMapper;
        this.accountRepo = accountRepository;
        this.projectRepo = projectRepository;
    }


    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic2.name}", groupId = "${spring.kafka.consumer2.group-id}")
    public void consume(String jsonEvent) {
        try
        {
            Map<String, Object> eventMap = objectMapper.readValue(jsonEvent, new TypeReference<Map<String, Object>>() {
            });

            Number accountIdNumber = (Number) eventMap.get("id");
            Long accountId = accountIdNumber.longValue();
            String email = (String) eventMap.get("email");

            LOGGER.info(String.format("User deleted event received in project service => %s", accountId.toString()));

            Optional<ProjectMember> projectMember = Optional.ofNullable(accountRepo.FindProjectMember(accountId, email));
            LOGGER.info(String.format("User delete 2event received in project service => %s", projectMember.toString()));
            if(projectMember.isPresent())
            {
                projectRepo.deleteAllProjectsFromOwner(projectMember.get().GetId());
                accountRepo.delete(projectMember.get());
            }

        }
        catch(JsonProcessingException e)
        {
            LOGGER.error("Error deserializing JSON message", e);
        }

    }

}