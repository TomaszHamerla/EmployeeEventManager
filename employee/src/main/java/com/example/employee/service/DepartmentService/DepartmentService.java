package com.example.employee.service.DepartmentService;

import com.example.employee.model.Department;
import com.example.employee.model.Employee;
import com.example.employee.model.dto.DepartmentDto;
import com.example.employee.model.dto.DepartmentEmployeeDto;

import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(Department department);
    DepartmentDto readDepartment(Long departmentId);
    List<DepartmentDto> readDepartments();
    DepartmentEmployeeDto addEmployee(Long departmentId, Long employeeId);
    void deleteEmployee(Long departmentId,Long employeeId);

}
