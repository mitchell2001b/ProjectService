package com.example.ProjectService.Events;

import com.example.ProjectService.Project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ProjectCreatedEvent implements Serializable
{
    private long Id;

    private String ProjectName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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

    public long GetId()
    {
        return Id;
    }

    public String GetProjectName()
    {
        return ProjectName;
    }

    public Date GetCreatedat()
    {
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
