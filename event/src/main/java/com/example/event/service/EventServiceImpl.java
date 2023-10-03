package com.example.event.service;

import com.example.event.exception.EventError;
import com.example.event.exception.EventException;
import com.example.event.model.EmployeeMember;
import com.example.event.model.Event;
import com.example.event.model.dto.Employee;
import com.example.event.repository.EmployeeServiceClient;
import com.example.event.repository.EventRepository;
import org.springframework.stereotype.Service;



import java.util.List;
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EmployeeServiceClient employeeServiceClient;

    public EventServiceImpl(EventRepository eventRepository, EmployeeServiceClient employeeServiceClient) {
        this.eventRepository = eventRepository;
        this.employeeServiceClient = employeeServiceClient;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> readEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event readEventByName(String name) {
        return getEvent(name);
    }


    @Override
    public List<Employee> readEmployees(String name) {
       return null;

    }

    @Override
    public Employee addEmployee(String name, Long employeeId) {

        Event event = getEvent(name);
        Employee employee = employeeServiceClient.getEmployee(employeeId);
        event.getEmployeeMembers().add(new EmployeeMember(employee.getEmail()));
        eventRepository.save(event);
        return employee;
    }
    private Event getEvent(String name) {
        return eventRepository.readByName(name)
                .orElseThrow(() -> new EventException(EventError.EVENT_NOT_FOUND_EXCEPTION));
    }
}
