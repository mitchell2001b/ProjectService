package com.example.ProjectService.ProjectRole.Project;

import com.example.ProjectService.ProjectMember.ProjectMember;
import com.example.ProjectService.JoinTable.ProjectMembership;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project
{

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 20)
    private  String Projectname;
    @Column(nullable = false, length = 50)
    private String Projectdescription;
    @Column(nullable = false)
    private Date Createdat;
    @Column(nullable = false)
    private Date Lastupdatedat;
    @JsonIgnore
    @ManyToOne()
    private ProjectMember Owner;

    @JsonIgnore
    @OneToMany(mappedBy = "Joinedproject", orphanRemoval = true, fetch = FetchType.LAZY)
    public Set<ProjectMembership>  Memberships = new HashSet<>();

    public Project(Long id, String projectdescription, Date createdat, Date lastupdatedat, ProjectMember owner, Set<ProjectMembership> memberships, String name)
    {
        Id = id;
        Projectdescription = projectdescription;
        Createdat = createdat;
        Lastupdatedat = lastupdatedat;
        Owner = owner;
        Memberships = memberships;
        Projectname = name;
    }
    public Project(String projectdescription, Date createdat, Date lastupdatedat, ProjectMember owner, Set<ProjectMembership> memberships, String name)
    {
        Projectdescription = projectdescription;
        Createdat = createdat;
        Lastupdatedat = lastupdatedat;
        Owner = owner;
        Memberships = memberships;
        Projectname = name;
    }

    public Project(String projectdescription, Date createdat, Date lastupdatedat, ProjectMember owner, String name)
    {
        Projectdescription = projectdescription;
        Createdat = createdat;
        Lastupdatedat = lastupdatedat;
        Owner = owner;
        Projectname = name;
    }

    public Project()
    {

    }

    public String getProjectname()
    {
        return Projectname;
    }

    public Long GetId()
    {
        return Id;
    }

    public String GetProjectdescription()
    {
        return Projectdescription;
    }

    public Date GetCreatedat()
    {
        return Createdat;
    }

    public Date GetLastupdatedat()
    {
        return Lastupdatedat;
    }

    public ProjectMember GetOwner()
    {
        return Owner;
    }

    public Set<ProjectMembership> GetMemberships()
    {
        return Memberships;
    }

    @Override
    public String toString() {
        return "Project{" +
                "Id=" + Id +
                ", Projectname='" + Projectname + '\'' +
                ", Projectdescription='" + Projectdescription + '\'' +
                ", Createdat=" + Createdat +
                ", Lastupdatedat=" + Lastupdatedat +
                ", Owner=" + Owner +
                ", Memberships=" + Memberships +
                '}';
    }
}
