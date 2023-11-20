package com.example.ProjectService;

import com.example.ProjectService.Project.Project;
import com.example.ProjectService.Project.dtos.ProjectDto;
import com.example.ProjectService.Project.dtos.ProjectMemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ProjectServiceApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetAll3ProjectsFromAccount() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProjectMemberDto owner = new ProjectMemberDto(140L, "testuser140@gmail.com");

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(owner))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andReturn();

    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetNoProjectsFromAccount() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProjectMemberDto owner = new ProjectMemberDto(142L, "testuser142@gmail.com");

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(owner))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldGetNoProjectsFromAccounts() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/myprojects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateProject() throws Exception {

        ProjectMemberDto owner = new ProjectMemberDto(140L, "testuser140@gmail.com");
        ProjectDto project = new ProjectDto("description1", new Date(), owner, "project66"); // Adjust the data as needed

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1));// Assuming the response contains the project ID
    }

    @Test
    @Sql("/AddTestData2.sql")
    void shouldCreateProjectScenario2() throws Exception {

        ProjectMemberDto owner = new ProjectMemberDto(140L, "testuser140@gmail.com");
        ProjectDto project = new ProjectDto("description16666", new Date(), owner, "project666666");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(jsonPath("id").value(1));// Assuming the response contains the project ID
    }

    @Test
    @Sql("/AddTestData.sql")
    void shouldNotCreateProject() throws Exception {

        ProjectMemberDto owner = new ProjectMemberDto(140L, "testuser140@gmail.com");
        ProjectDto project = new ProjectDto("description16666", new Date(), owner, "project666666");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());


    }
}