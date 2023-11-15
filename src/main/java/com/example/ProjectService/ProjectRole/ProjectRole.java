package com.example.ProjectService.ProjectRole;

import jakarta.persistence.*;

@Entity
@Table(name = "projectrole")
public class ProjectRole
{
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique=true)
    private String Name;

    public ProjectRole(Long id, String name)
    {
        Id = id;
        Name = name;
    }
    public ProjectRole()
    {

    }

    public Long GetId()
    {
        return Id;
    }

    public String GetName()
    {
        return Name;
    }
}
