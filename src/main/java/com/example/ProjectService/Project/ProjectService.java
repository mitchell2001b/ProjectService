package com.example.ProjectService.Project;

import com.example.ProjectService.repositories.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService
{
    private IProjectRepository ProjectRepository;

    @Autowired
    public ProjectService(IProjectRepository projectRepository)
    {
        this.ProjectRepository = projectRepository;
    }

    public Optional<Project> SelectProjectById(int id)
    {
        return this.ProjectRepository.findById(id);
    }

}
