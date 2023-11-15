package com.example.ProjectService.Project;

import com.example.ProjectService.JoinTable.ProjectMembership;
import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.dtos.ProjectDto;
import com.example.ProjectService.dtos.ProjectMemberDto;
import com.example.ProjectService.repositories.IAccountRepository;
import com.example.ProjectService.repositories.IProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
    private IProjectRepository ProjectRepository;
    private IAccountRepository AccountRepository;

    @Autowired
    public ProjectService(IProjectRepository projectRepository, IAccountRepository accountRepository)
    {
        this.ProjectRepository = projectRepository;
        this.AccountRepository = accountRepository;
    }

    public List<ProjectDto> GetAllProjectsFromOwner(Long ownerId)
    {
        Optional<ProjectMember> owner = this.AccountRepository.findById(ownerId);
        ProjectMemberDto ownerDto = new ProjectMemberDto(ownerId, owner.get().GetEmail());
        List<ProjectDto> projectList = new ArrayList<>();

        for (Project project : this.ProjectRepository.getAllProjectsFromOwner(ownerId))
        {
           ProjectDto projectDto = new ProjectDto(project.GetId(), project.GetProjectdescription(), project.GetLastupdatedat(), ownerDto, project.getProjectname());
           projectList.add(projectDto);
        }

        return projectList;
    }
    public Optional<Project> SelectProjectById(Long id)
    {
        return this.ProjectRepository.findById(id);
    }

    public Project CreateProject(ProjectDto newProject)
    {


        Optional<ProjectMember> owner = AccountRepository.findById(newProject.GetOwner().GetId());

        if(owner != null)
        {
            ProjectMember member = new ProjectMember(newProject.GetOwner().GetId(), newProject.GetOwner().GetEmail());
            AccountRepository.save(member);
        }
        Project project = new Project(newProject.GetProjectDescription(), new Date(), new Date(), owner.get(), newProject.GetProjectName());
        LOGGER.info(String.format("new project dto => %s", project.toString()));
        ProjectRepository.save(project);
        return this.ProjectRepository.findLastCreatedProject();
        //return null;
    }

}
