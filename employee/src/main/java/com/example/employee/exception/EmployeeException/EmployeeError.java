package com.example.employee.exception.EmployeeException;

import com.fasterxml.jackson.annotation.JsonFormat;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EmployeeError {
    EMPLOYEE_ID_NOT_FOUND("employee with given id not found !"),
    EMPLOYEE_STATUS_INACTIVE("employee status is INACTIVE !"),
    EMPLOYEE_ALREADY_EXISTS_IN_SOME_DEPARTMENT("Employee with given id already exists in some department !"),
    EMPLOYEE_EMAIL_ALREADY_EXISTS("Employee with given email already exists !"),
    EMPLOYEE_POSITION_CAN_NOT_BE_BLANK("Can not create employee with blank position !");
    private String message;

    EmployeeError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
