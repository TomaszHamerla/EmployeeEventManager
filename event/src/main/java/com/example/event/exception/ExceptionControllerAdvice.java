package com.example.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(EventException.class)
    ResponseEntity<?>exceptionHandler(EventException e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (EventError.EVENT_NOT_FOUND_EXCEPTION.equals(e.getEventError())){
            status=HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(e.getEventError());
    }
}
