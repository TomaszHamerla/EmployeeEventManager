package com.example.employee.exception.EmployeeException;

public class EmployeeException extends RuntimeException {
  private EmployeeError employeeError;



    public EmployeeException(EmployeeError exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.employeeError=exceptionMessage;
    }

    public EmployeeError getEmployeeError() {
        return employeeError;
    }
}
