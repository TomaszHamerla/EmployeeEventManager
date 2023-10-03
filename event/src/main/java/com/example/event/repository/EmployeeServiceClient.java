package com.example.event.repository;

import com.example.event.model.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("EMPLOYEE-SERVICE")
public interface EmployeeServiceClient {
    @GetMapping("/employees/{employeeId}")
    Employee getEmployee(@PathVariable Long employeeId);
}
