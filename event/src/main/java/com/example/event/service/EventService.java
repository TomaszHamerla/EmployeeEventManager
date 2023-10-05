package com.example.event.service;

import com.example.event.model.Event;
import com.example.event.model.dto.Employee;

import java.util.List;


public interface EventService {
    Event createEvent(Event event);
    List<Event>readEvents();
    Event readEventByName(String name);
    List<Employee>readEmployees(String name);
    Employee addEmployee(String name,Long employeeId);
    void finishEventEnrollment(String eventName);
    void removeEmployee(String name,String email);
    void deactivationEvent(String eventName);
}
