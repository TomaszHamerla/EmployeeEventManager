package com.example.employee.exception;

import com.example.employee.exception.DepartmentException.DepartmentError;
import com.example.employee.exception.DepartmentException.DepartmentException;
import com.example.employee.exception.EmployeeException.EmployeeError;
import com.example.employee.exception.EmployeeException.EmployeeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<EmployeeError> exceptionHandler(EmployeeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (EmployeeError.EMPLOYEE_ID_NOT_FOUND.equals(e.getEmployeeError())) {
            status = HttpStatus.NOT_FOUND;
        } else if (EmployeeError.EMPLOYEE_STATUS_INACTIVE.equals(e.getEmployeeError())) {
            status = HttpStatus.BAD_REQUEST;
        } else if (EmployeeError.EMPLOYEE_ALREADY_EXISTS_IN_SOME_DEPARTMENT.equals(e.getEmployeeError())) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (EmployeeError.EMPLOYEE_EMAIL_ALREADY_EXISTS.equals(e.getEmployeeError())) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(e.getEmployeeError());
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<DepartmentError> exceptionHandler(DepartmentException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (DepartmentError.DEPARTMENT_ID_NOT_FOUND.equals(e.getDepartmentError())) {
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(e.getDepartmentError());
    }
}
