package com.example.ProjectService.dtos;

import com.example.ProjectService.JoinTable.ProjectMembership;
import com.example.ProjectService.ProjectMember.ProjectMember;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ProjectDto implements Serializable
{
     @JsonProperty("id")
     private Long Id;

     @JsonProperty("projectdescription")
     private String ProjectDescription;

     @JsonProperty("projectname")
     private String ProjectName;
     @JsonProperty("lastupdatedat")
     @JsonFormat(pattern = "yyyy-MM-dd")
     private Date Lastupdatedat;

     @JsonProperty("owner")
     private ProjectMemberDto Owner;

     @JsonProperty("projectmemberships")
     private List<ProjectMembershipDto> ProjectMemberships = new ArrayList<>();

     public ProjectDto(Long id, String projectDescription, Date lastupdatedat, ProjectMemberDto owner, List<ProjectMembershipDto> projectMemberships, String name)
     {
          Id = id;
          ProjectDescription = projectDescription;
          Lastupdatedat = lastupdatedat;
          Owner = owner;
          ProjectMemberships = projectMemberships;
          ProjectName = name;
     }
     public ProjectDto(Long id, String projectDescription, Date lastupdatedat, ProjectMemberDto owner, String name)
     {
          Id = id;
          ProjectDescription = projectDescription;
          Lastupdatedat = lastupdatedat;
          Owner = owner;
          ProjectName = name;
     }

     public ProjectDto(String projectDescription, Date lastupdatedat, ProjectMemberDto owner, List<ProjectMembershipDto> projectMemberships, String name)
     {
          ProjectDescription = projectDescription;
          Lastupdatedat = lastupdatedat;
          Owner = owner;
          ProjectMemberships = projectMemberships;
          ProjectName = name;
     }

     public ProjectDto(String projectDescription, Date lastupdatedat, ProjectMemberDto owner, String name)
     {
          ProjectDescription = projectDescription;
          Lastupdatedat = lastupdatedat;
          Owner = owner;
          ProjectName = name;
     }
     public ProjectDto(String projectDescription, ProjectMemberDto owner, String name)
     {
          ProjectDescription = projectDescription;
          Owner = owner;
          ProjectName = name;
     }
     public ProjectDto()
     {

     }

     public Long GetId()
     {
          return Id;
     }

     public String GetProjectName() {
          return ProjectName;
     }

     public String GetProjectDescription()
     {
          return ProjectDescription;
     }

     public Date GetLastupdatedat()
     {
          return Lastupdatedat;
     }

     public ProjectMemberDto GetOwner()
     {
          return Owner;
     }

     public List<ProjectMembershipDto> GetProjectMemberships()
     {
          return ProjectMemberships;
     }

     @Override
     public String toString() {
          return "ProjectDto{" +
                  "Id=" + Id +
                  ", ProjectDescription='" + ProjectDescription + '\'' +
                  ", ProjectName='" + ProjectName + '\'' +
                  ", Lastupdatedat=" + Lastupdatedat +
                  ", Owner=" + Owner +
                  ", ProjectMemberships=" + ProjectMemberships +
                  '}';
     }
}
