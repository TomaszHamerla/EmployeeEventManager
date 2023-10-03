package com.example.employee.controller;

import com.example.employee.model.Department;
import com.example.employee.model.dto.DepartmentDto;
import com.example.employee.model.dto.DepartmentEmployeeDto;
import com.example.employee.service.DepartmentService.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @PostMapping
    ResponseEntity<DepartmentDto>createDepartment(@RequestBody @Valid Department department){
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }
    @GetMapping
    ResponseEntity<List<DepartmentDto>>readDepartments(){
        return ResponseEntity.ok(departmentService.readDepartments());
    }
    @GetMapping("/{departmentId}")
    ResponseEntity<DepartmentDto>readDepartment(@PathVariable Long departmentId){
        return ResponseEntity.ok(departmentService.readDepartment(departmentId));
    }
    @PostMapping("/{departmentId}/employees/{employeeId}")
    ResponseEntity<DepartmentEmployeeDto>addEmployeeToDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        return ResponseEntity.ok(departmentService.addEmployee(departmentId,employeeId));
    }
    @DeleteMapping("/{departmentId}/employees/{employeeId}")
    void deleteEmployee(@PathVariable Long departmentId,@PathVariable Long employeeId){
        departmentService.deleteEmployee(departmentId,employeeId);
    }

}
