package com.example.ProjectService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProjectMemberDto implements Serializable
{
    @JsonProperty("id")
    private Long Id;

    @JsonProperty("email")
    private String Email;

    public ProjectMemberDto(Long id, String email)
    {
        Id = id;
        Email = email;
    }
    public ProjectMemberDto(Long id)
    {
        Id = id;
    }

    public ProjectMemberDto()
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

    @Override
    public String toString() {
        return "ProjectMemberDto{" +
                "Id=" + Id +
                ", Email='" + Email + '\'' +
                '}';
    }
}
