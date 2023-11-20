package com.example.ProjectService.Project;

import com.example.ProjectService.Project.dtos.ProjectDto;
import com.example.ProjectService.Project.dtos.ProjectMemberDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8585, http://api-gateway:8585, http://apigateway:8585, http://localhost:8787, http://taskboarding-frontend:8787, http://localhost:3001, http://taskboarding-frontend:3001"})
@RequestMapping(path = "api/projects")
public class ProjectController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService service)
    {
        this.projectService = service;
    }
    @PostMapping(value = "/create")
    public ResponseEntity<String> CreateProject(@RequestBody ProjectDto newProject)
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
                .body("{ \"id\": "+ project.GetId() + " }");
    }

    @PostMapping(value = "/myprojects")
    public ResponseEntity<List<ProjectDto>> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto)
    {
        if(ownerDto == null)
        {
            return ResponseEntity.badRequest().build();
        }

        List<ProjectDto> dtos = this.projectService.GetAllProjectsFromOwner(ownerDto);
        return ResponseEntity.ok(dtos);
    }
    @GetMapping(value = "/tess")
    public String tess()
    {
        return "tes dit is tes";
    }
}
