package com.example.ProjectService.Account;

import com.example.ProjectService.Project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "account")
public class Account
{
    @jakarta.persistence.Id
    private int Id;

    @Column(unique=true)
    private String Email;

    @ManyToMany(mappedBy = "ProjectMembers")
    private Set<Project> Projects;

    @JsonIgnore
    @OneToMany(mappedBy = "Leader", orphanRemoval = true)
    public Set<Project> LedProjects = new HashSet<>();

    public Account(int id, String email, Set<Project> projects, Set<Project> ledProjects)
    {
        Id = id;
        Email = email;
        Projects = projects;
        LedProjects = ledProjects;
    }

    public Account(int id, String email)
    {
        Id = id;
        Email = email;
    }

    public Account()
    {

    }

    public int getId()
    {
        return Id;
    }

    public String getEmail()
    {
        return Email;
    }

    public Set<Project> getProjects()
    {
        return Projects;
    }

    public Set<Project> getLedProjects()
    {
        return LedProjects;
    }

    @Override
    public String toString() {
        return "Account{" +
                "Id=" + Id +
                ", Email='" + Email + '\'' +
                ", Projects=" + Projects +
                ", LedProjects=" + LedProjects +
                '}';
    }
}
