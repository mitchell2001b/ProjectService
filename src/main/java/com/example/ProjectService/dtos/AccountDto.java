package com.example.ProjectService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AccountDto implements Serializable
{
    @JsonProperty("id")
    private int Id;

    @JsonProperty("email")
    private String Email;



}
