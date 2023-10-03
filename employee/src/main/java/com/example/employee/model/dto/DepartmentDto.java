package com.example.employee.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DepartmentDto {
    private Long id;
    private String name;
    private String description;
    private List<DepartmentEmployeeDto>employees;
}
