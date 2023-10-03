package com.example.employee.service.operations;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Status;

public interface EmployeeValidations {
    Employee getEmployee(Long employeeID);
    void validStatus(Status status);
}
