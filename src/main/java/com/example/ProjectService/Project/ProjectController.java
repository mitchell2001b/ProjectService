package com.example.ProjectService.Project;

import com.example.ProjectService.dtos.ProjectDto;
import com.example.ProjectService.dtos.ProjectMemberDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8585, http://api-gateway:8585, http://apigateway:8585, http://localhost:8787, http://taskboarding-frontend:8787, http://localhost:3000, http://taskboarding-frontend:3000"})
@RequestMapping(path = "api/projects")
public class ProjectController
{
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
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{could not create project}");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{ \"id\": "+ project.GetId() + " }");
    }

    @PostMapping(value = "myprojects")
    public List<ProjectDto> GetAllProjectsFromOwner(@RequestBody ProjectMemberDto ownerDto)
    {
        System.out.println(ownerDto.toString());
        System.out.println("gggg");
        return this.projectService.GetAllProjectsFromOwner(ownerDto.GetId());
    }
    @GetMapping(value = "/tess")
    public String tess()
    {
        return "tes dit is tes";
    }
}
