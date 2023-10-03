package com.example.employee.exception.DepartmentException;

public class DepartmentException extends RuntimeException {
    private DepartmentError departmentError;

    public DepartmentException(DepartmentError departmentError) {
        super(departmentError.getMessage());
        this.departmentError=departmentError;
    }

    public DepartmentError getDepartmentError() {
        return departmentError;
    }
}
