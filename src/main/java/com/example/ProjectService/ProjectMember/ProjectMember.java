package com.example.ProjectService.ProjectMember;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;


@Entity
@Table(name = "projectmember")
public class ProjectMember
{
    @jakarta.persistence.Id
    private Long Id;

    @Column(unique=true)
    private String Email;

    public ProjectMember(Long id, String email)
    {
        Id = id;
        Email = email;
    }

    public ProjectMember()
    {

    }

    public Long GetId()
    {
        return Id;
    }

    public String GetEmail()
    {
        return Email;
    }
}
