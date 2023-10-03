package com.example.employee.service.EmployeeService;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(Employee employee);
    EmployeeDto readEmployee(Long employeeID);

    List<EmployeeDto> readEmployees(Position position);

    void deactivationEmployee(Long employeeId);
    EmployeeDto updateEmployee(Long employeeId,Employee employee); // allows to update lastName, email and position

}
