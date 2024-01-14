package com.example.ProjectService.Events;

import java.io.Serializable;
import java.util.Date;

public class ProjectCreatedEvent implements Serializable
{
    private long Id;

    private String ProjectName;

    private Date Createdat;

    public ProjectCreatedEvent(long id, String projectName, Date createdat)
    {
        this.Id = id;
        this.ProjectName = projectName;
        this.Createdat = createdat;
    }

    public ProjectCreatedEvent()
    {

    }

    public long getId() {
        return Id;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public Date getCreatedat() {
        return Createdat;
    }

    @Override
    public String toString()
    {
        return "ProjectCreatedEvent{" +
                "Id=" + Id +
                ", ProjectName='" + ProjectName + '\'' +
                ", Createdat=" + Createdat +
                '}';
    }
}
