package com.example.event.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String status;
}
