package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.dto.EmployeeDto;
import com.example.employee.service.EmployeeService.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    ResponseEntity<EmployeeDto>createEmployee(@RequestBody @Valid Employee employee){
        EmployeeDto createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.created(URI.create("/"+createdEmployee.getId())).body(createdEmployee);
    }
    @GetMapping()
    ResponseEntity<List<EmployeeDto>>readEmployees(@RequestParam(required = false)Position position){
        return ResponseEntity.ok(employeeService.readEmployees(position));
    }
    @GetMapping("/{employeeId}")
    ResponseEntity<EmployeeDto>readEmployee(@PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.readEmployee(employeeId));
    }
    @PostMapping("/{emails}")
    ResponseEntity<List<EmployeeDto>>readEmployeesByEmails(@PathVariable List<String>emails){
        return ResponseEntity.ok(employeeService.readEmployeesByEmails(emails));
    }
    @PatchMapping("/{employeeId}")
    ResponseEntity<EmployeeDto>updateEmployee(@PathVariable Long employeeId,@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId,employee));
    }
    @DeleteMapping("/{employeeId}")
    ResponseEntity<?>deactivationEmployee(@PathVariable Long employeeId){
        employeeService.deactivationEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
