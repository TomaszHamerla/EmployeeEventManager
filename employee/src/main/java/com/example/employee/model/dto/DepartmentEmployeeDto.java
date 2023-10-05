package com.example.employee.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DepartmentEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;

}
