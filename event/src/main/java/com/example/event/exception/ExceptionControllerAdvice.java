package com.example.event.exception;

import feign.FeignException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(EventException.class)
    ResponseEntity<EventError>exceptionHandler(EventException e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (EventError.EVENT_NOT_FOUND_EXCEPTION.equals(e.getEventError())){
            status=HttpStatus.NOT_FOUND;
        }
        else if (EventError.EMPLOYEE_IS_NOT_ACTIVE.equals(e.getEventError())){
            status=HttpStatus.METHOD_NOT_ALLOWED;
        }
        return ResponseEntity.status(status).body(e.getEventError());
    }
    @ExceptionHandler(FeignException.class)
    ResponseEntity<?>exceptionHandler(FeignException e){
        return ResponseEntity.status(e.status()).body(new JSONObject(e.contentUTF8()).toMap());
    }
}
