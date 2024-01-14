package com.example.ProjectService.JoinTable;

import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.Project.Project;
import com.example.ProjectService.ProjectRole.ProjectRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projectmembership")
public class ProjectMembership
{
    @Id
    @ManyToOne
    private ProjectMember Member;

    @Id
    @ManyToOne
    private ProjectRole Role;

    @Id
    @ManyToOne
    private Project Joinedproject;

    public ProjectMembership(ProjectMember member, ProjectRole role, Project joinedProject)
    {
        Member = member;
        Role = role;
        Joinedproject = joinedProject;
    }
    public ProjectMembership()
    {

    }

    public ProjectMember GetMember()
    {
        return Member;
    }

    public ProjectRole GetRole()
    {
        return Role;
    }

    public Project GetJoinedProject()
    {
        return Joinedproject;
    }
}
