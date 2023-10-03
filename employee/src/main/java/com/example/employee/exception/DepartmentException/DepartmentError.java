package com.example.employee.exception.DepartmentException;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DepartmentError {
    DEPARTMENT_ID_NOT_FOUND("Department with given id not found !");
    private String message;

    DepartmentError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
