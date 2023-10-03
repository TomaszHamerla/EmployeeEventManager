package com.example.event.exception;

public class EventException extends RuntimeException {
    private EventError eventError;
    public EventException(EventError eventError) {
        super(eventError.getMessage());
        this.eventError=eventError;
    }

    public EventError getEventError() {
        return eventError;
    }
}
