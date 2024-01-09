package com.example.ProjectService.Project;

import com.example.ProjectService.Project.dtos.ProjectDto;
import com.example.ProjectService.Project.dtos.ProjectMemberDto;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/projects")
public class ProjectController
{
    private static final String ENVIRONMENT_PROPERTY = "spring.profiles.active";
    private static final String TEST_PROFILE = "test";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService service)
    {
        this.projectService = service;
    }






    public static boolean isTestingEnvironment()
    {
        String activeProfiles = System.getProperty(ENVIRONMENT_PROPERTY);
        return activeProfiles != null && activeProfiles.contains(TEST_PROFILE);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> CreateProject(@RequestBody ProjectDto newProject, @RequestHeader(name = "Authorization") String authorizationHeader)
    {
        Project project = null;
        try
        {
            project = projectService.CreateProject(newProject);
        }
        catch (Exception e)
        {
            LOGGER.error("Error creating project", e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Project created successfully");
    }


    /*@PostMapping(value = "/myprojects")
    public ResponseEntity<List<ProjectDto>> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto)
    {
        if(ownerDto == null)
        {
            return ResponseEntity.badRequest().build();
        }
        List<ProjectDto> dtos = null;

        try
        {
            dtos = this.projectService.GetAllProjectsFromOwner(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisConnectionFailureException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisConnectionException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (RedisCommandTimeoutException e)
        {
            dtos = this.projectService.GetAllProjectsFromOwnerInDb(ownerDto);
            return ResponseEntity.ok(dtos);
        }
        catch (Exception e)
        {
            LOGGER.error("Error getting my projects", e);
        }
        return null;

    }*/
    @PostMapping(value = "/myprojects")
    public CompletableFuture<ResponseEntity<List<ProjectDto>>> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto, @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (ownerDto == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        if(isTestingEnvironment())
        {
            //use testing key
        }
        else
        {
            //use real key
        }

        return this.projectService.GetAllProjectsFromOwnerInDbAsync(ownerDto)
                .thenApply(dtos -> ResponseEntity.ok(dtos))
                .exceptionally(ex -> {
                    LOGGER.error("Error getting my projects", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
    @GetMapping(value = "/tess")
    public String tess()
    {
        return "tes dit is tes";
    }
}



