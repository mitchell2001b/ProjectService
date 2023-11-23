package com.example.ProjectService.Project;

import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.Project.dtos.ProjectDto;
import com.example.ProjectService.Project.dtos.ProjectMemberDto;
import com.example.ProjectService.repositories.IAccountRepository;
import com.example.ProjectService.repositories.IProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "projectsByOwner")
public class ProjectService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
    private IProjectRepository projectRepository;
    private IAccountRepository accountRepository;

    @Autowired
    public ProjectService(IProjectRepository projectRepo, IAccountRepository accountRepo)
    {
        this.projectRepository = projectRepo;
        this.accountRepository = accountRepo;
    }
    @Cacheable(value = "projectsByOwner", key = "#givenOwnerDto.GetId()")
    public List<ProjectDto> GetAllProjectsFromOwner(ProjectMemberDto givenOwnerDto)
    {
        List<ProjectDto> projectList = new ArrayList<>();
        try
        {
            Optional<ProjectMember> owner = this.accountRepository.findById(givenOwnerDto.GetId());
            ProjectMemberDto ownerDto;
            if(owner.isEmpty())
            {
                ProjectMember pm = new ProjectMember(givenOwnerDto.GetId(), givenOwnerDto.GetEmail());
                this.accountRepository.save(pm);
                ownerDto = givenOwnerDto;
            }
            else
            {
                ownerDto = new ProjectMemberDto(givenOwnerDto.GetId(), owner.get().GetEmail());
            }



            for (Project project : this.projectRepository.getAllProjectsFromOwner(givenOwnerDto.GetId()))
            {
                ProjectDto projectDto = new ProjectDto(project.GetId(), project.GetProjectdescription(), project.GetLastupdatedat(), ownerDto, project.getProjectname());
                projectList.add(projectDto);
            }

        }
        catch (Exception e)
        {
            //throw new RuntimeException("Error in GetAllProjectsFromOwner", e);
        }
        return projectList;
    }
    public List<ProjectDto> GetAllProjectsFromOwnerInDb(ProjectMemberDto givenOwnerDto)
    {
        List<ProjectDto> projectList = new ArrayList<>();
        try
        {
            Optional<ProjectMember> owner = this.accountRepository.findById(givenOwnerDto.GetId());
            ProjectMemberDto ownerDto;
            if(owner.isEmpty())
            {
                ProjectMember pm = new ProjectMember(givenOwnerDto.GetId(), givenOwnerDto.GetEmail());
                this.accountRepository.save(pm);
                ownerDto = givenOwnerDto;
            }
            else
            {
                ownerDto = new ProjectMemberDto(givenOwnerDto.GetId(), owner.get().GetEmail());
            }

            for (Project project : this.projectRepository.getAllProjectsFromOwner(givenOwnerDto.GetId()))
            {
                ProjectDto projectDto = new ProjectDto(project.GetId(), project.GetProjectdescription(), project.GetLastupdatedat(), ownerDto, project.getProjectname());
                projectList.add(projectDto);
            }


        }
        catch (Exception e)
        {
            //throw new RuntimeException("Error in GetAllProjectsFromOwner", e);
        }
        return projectList;
    }

    public Optional<Project> SelectProjectById(Long id)
    {
        return this.projectRepository.findById(id);
    }

    public Project CreateProject(ProjectDto newProject)
    {
        Optional<ProjectMember> owner = accountRepository.findById(newProject.GetOwner().GetId());

        if(!owner.isPresent())
        {
            ProjectMember member = new ProjectMember(newProject.GetOwner().GetId(), newProject.GetOwner().GetEmail());
            accountRepository.save(member);
            owner = accountRepository.findById(newProject.GetOwner().GetId());
        }

        if(!owner.isEmpty())
        {
            Project project = new Project(newProject.GetProjectDescription(), new Date(), new Date(), owner.get(), newProject.GetProjectName());
            LOGGER.info(String.format("new project dto => %s", project.toString()));

            projectRepository.save(project);

        }

        return this.projectRepository.findLastCreatedProject();
        //return null;
    }
    @CacheEvict(value = "projectsByOwner", allEntries = true)
    public void ResetProjectsByOwnerCache()
    {
        LOGGER.info("Resetting the cache");
    }

}
