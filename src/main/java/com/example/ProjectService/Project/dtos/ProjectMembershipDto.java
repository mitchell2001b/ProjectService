package com.example.ProjectService.Project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProjectMembershipDto implements Serializable
{
    @JsonProperty("projectmember")
    private ProjectMemberDto ProjectMember;

    @JsonProperty("projectrole")
    private ProjectRoleDto ProjectRole;

    public ProjectMembershipDto(ProjectMemberDto projectMember, ProjectRoleDto projectRole)
    {
        ProjectMember = projectMember;
        ProjectRole = projectRole;
    }

    public ProjectMembershipDto()
    {

    }

    public ProjectMemberDto GetProjectMember() {
        return ProjectMember;
    }

    public ProjectRoleDto GetProjectRole() {
        return ProjectRole;
    }
}
