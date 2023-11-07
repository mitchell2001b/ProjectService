package com.example.ProjectService.Project;

import com.example.ProjectService.Account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project
{

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String Projectdescription;

    private Date Createdat;

    private Date Lastupdatedat;

    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account Leader;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_account",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> ProjectMembers;

    public Project(int id, String projectdescription, Date createdat, Date lastupdatedat, Account leader, Set<Account> projectMembers)
    {
        Id = id;
        Projectdescription = projectdescription;
        Createdat = createdat;
        Lastupdatedat = lastupdatedat;
        Leader = leader;
        ProjectMembers = projectMembers;
    }

    public Project(String projectdescription, Date createdat, Date lastupdatedat, Account leader, Set<Account> projectMembers)
    {
        Projectdescription = projectdescription;
        Createdat = createdat;
        Lastupdatedat = lastupdatedat;
        Leader = leader;
        ProjectMembers = projectMembers;
    }

    public Project()
    {

    }

    public int getId()
    {
        return Id;
    }

    public String getProjectdescription()
    {
        return Projectdescription;
    }

    public Date getCreatedat()
    {
        return Createdat;
    }

    public Date getLastupdatedat()
    {
        return Lastupdatedat;
    }

    public Account getLeader()
    {
        return Leader;
    }

    public Set<Account> getProjectMembers()
    {
        return ProjectMembers;
    }

    @Override
    public String toString() {
        return "Project{" +
                "Id=" + Id +
                ", Projectdescription='" + Projectdescription + '\'' +
                ", Createdat=" + Createdat +
                ", Lastupdatedat=" + Lastupdatedat +
                ", Leader=" + Leader +
                ", ProjectMembers=" + ProjectMembers +
                '}';
    }
}
