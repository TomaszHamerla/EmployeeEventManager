package com.example.event.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventError {
    EVENT_NOT_FOUND_EXCEPTION("Event with given name not found !"),
    EVENT_DATE_CONFLICT("EndDate can not be earlier then startDate !"),
    EVENT_FULL_PARTICIPANTS_NUMBER("Can not add another employee to this event because limit is full !"),
    EVENT_NOT_AVAILABLE_STATUS("Event with given name has not active status !"),
    EVENT_DUPLICATE_NAME("Can not create event with this name !"),
    EMPLOYEE_IS_NOT_ACTIVE("Employee with given id is not avitve !");
    private String message;

    EventError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
