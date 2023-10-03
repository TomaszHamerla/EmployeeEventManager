package com.example.event.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventError {
    EVENT_NOT_FOUND_EXCEPTION("Event with given name not fiund !");
    private String message;

    EventError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
