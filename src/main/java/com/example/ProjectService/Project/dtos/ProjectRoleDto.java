package com.example.ProjectService.Project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

public class ProjectRoleDto implements Serializable
{
    @JsonProperty("id")
    private Long Id;

    @JsonProperty("name")
    private String Name;



}
