package com.example.employee.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class EmployeeDto extends DepartmentEmployeeDto {
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd't'hh:mm")
    private LocalDateTime hireDate;
    private String departmentName;
    private String status;



}
