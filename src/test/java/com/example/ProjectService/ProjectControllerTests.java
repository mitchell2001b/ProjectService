package com.example.ProjectService;

import com.example.ProjectService.AzureServices.KeyVaultService;
import com.azure.security.keyvault.secrets.SecretClient;
import com.example.ProjectService.dtos.ProjectDto;
import com.example.ProjectService.dtos.ProjectMemberDto;
import com.example.ProjectService.kafka.ProjectProducer;
import com.example.ProjectService.kafka.RegistrationConsumer;
import com.example.ProjectService.kafka.RegistrationDeleteConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.crypto.SecretKey;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = { ProjectServiceApplication.class, KeyVaultService.class}

)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@AutoConfigureMockMvc()
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@MockBean({RegistrationConsumer.class, ProjectProducer.class, RegistrationDeleteConsumer.class, SecretClient.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(roles = {})
class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetAll3ProjectsFromAccount() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProjectMemberDto owner = new ProjectMemberDto(140L, "testusr140@gmail.com");
        String mockToken = CreateMockToken("testusr140@gmail.com", 140L, "user");
        var result = mockMvc.perform(post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(owner))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));

    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetNoProjectsFromAccount() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProjectMemberDto owner = new ProjectMemberDto(142L, "testusr142@gmail.com");
        String mockToken = CreateMockToken("testusr142@gmail.com", 142L, "user");
        var result = mockMvc.perform(post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(owner))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        //.andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetNoProjectsFromAccounts() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var result = mockMvc.perform(post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldCreateProject() throws Exception {

        //user already exists in Database
        ProjectMemberDto owner = new ProjectMemberDto(140L, "testusr140@gmail.com");
        ProjectDto project = new ProjectDto("description1", new Date(), owner, "project66");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String mockToken = CreateMockToken("testusr140@gmail.com", 140L, "user");


        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Project created successfully"));
    }

    @Test
    @Sql("/AddTestData2.sql")
    void shouldCreateProjectWithNewUser() throws Exception {

        //user doesn't exist in Database yet
        ProjectMemberDto owner = new ProjectMemberDto(144L, "testusr144@gmail.com");
        ProjectDto project = new ProjectDto("description16666", new Date(), owner, "project666666");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String mockToken = CreateMockToken("testusr144@gmail.com", 144L, "user");

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Project created successfully"));
    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldNotCreateProject() throws Exception {

        ProjectMemberDto owner = new ProjectMemberDto(140L, "testusr140@gmail.com");
        String mockToken = CreateMockToken("testusr140@gmail.com", 140L, "user");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }
    @Test
    @Sql("/AddTestData.sql")
    void shouldNotCreateProjectAndThrowConflicted() throws Exception {

        ProjectMemberDto owner = new ProjectMemberDto(140L, "testusr140@gmail.com");
        ProjectDto project = new ProjectDto("description16666", new Date(), owner, "thisnameiswaytolongforaprojectandsonotallowed");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String mockToken = CreateMockToken("testusr140@gmail.com", 140L, "user");

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    private String CreateMockToken(String email, Long id, String roleName) {

        String mockKey = "MockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectServiceMockKeyForSemester6TestInprojectService";
        Instant expirationDate = Instant.now().plusSeconds(3600);
        SecretKey signingKey = Keys.hmacShaKeyFor(mockKey.getBytes());

        return Jwts.builder()
                .claim("email", email)
                .claim("id", id)
                .claim("roleName", roleName)
                .setExpiration(Date.from(expirationDate))
                .signWith(signingKey)
                .compact();
    }
}