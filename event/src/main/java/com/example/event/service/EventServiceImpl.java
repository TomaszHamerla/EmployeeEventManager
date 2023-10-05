package com.example.event.service;

import com.example.event.exception.EventError;
import com.example.event.exception.EventException;
import com.example.event.model.EmployeeMember;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.model.dto.Employee;
import com.example.event.model.dto.Notification;
import com.example.event.repository.EmployeeServiceClient;
import com.example.event.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final RabbitTemplate rabbitTemplate;
    private static final String queueName = "test";
    private static final int participantsLimit=5;

    public EventServiceImpl(EventRepository eventRepository, EmployeeServiceClient employeeServiceClient, RabbitTemplate rabbitTemplate) {
        this.eventRepository = eventRepository;
        this.employeeServiceClient = employeeServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Event createEvent(Event event) {
        validEvent(event);
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
        Event event = getEvent(name);
        return employeeServiceClient.getEmployeesByEmails(getEmployeeEmails(event));
    }

    @Override
    public Employee addEmployee(String name, Long employeeId) {

        Event event = getEvent(name);
        checkEventStatus(event.getStatus());
        checkParticipantsNumber(event.getParticipantsNumber());
        Employee employee = employeeServiceClient.getEmployee(employeeId);
        validEmployee(employee.getStatus());
        event.getEmployeeMembers().add(new EmployeeMember(employee.getEmail()));
        event.setParticipantsNumber(event.getParticipantsNumber() + 1);
        checkParticipantsNumber(event);
        eventRepository.save(event);
        return employee;
    }


    @Override
    public void deactivationEvent(String eventName) {
        Event event = getEvent(eventName);
        event.setStatus(EventStatus.CANCEL);
        eventRepository.save(event);
    }

    @Override
    public void removeEmployee(String name, String email) {
        Event event = getEvent(name);
        event.getEmployeeMembers().removeIf(employeeMember -> employeeMember.getEmail().equals(email));
        event.setParticipantsNumber(event.getParticipantsNumber() - 1);
        eventRepository.save(event);
    }

    @Override
    public void finishEventEnrollment(String eventName) {
        Event event = getEvent(eventName);
        sendNotification(event);
        event.setStatus(EventStatus.COMPLETED);
        eventRepository.save(event);
    }

    private void sendNotification(Event event) {

            Notification notification = Notification.builder()
                    .emails(event.getEmployeeMembers().stream()
                            .map(EmployeeMember::getEmail).collect(Collectors.toList()))
                    .eventName(event.getName())
                    .startDate(event.getStartDate())
                    .build();
            rabbitTemplate.convertAndSend(queueName,notification);
            log.info(notification.toString());

    }

    private void validEvent(Event event) {
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new EventException(EventError.EVENT_DATE_CONFLICT);
        }
        if (eventRepository.existsByName(event.getName())) {
            throw new EventException(EventError.EVENT_DUPLICATE_NAME);
        }
    }

    private void checkEventStatus(EventStatus status) {
        if (EventStatus.CANCEL.equals(status)) {
            throw new EventException(EventError.EVENT_NOT_AVAILABLE_STATUS);
        }
    }

    private void checkParticipantsNumber(Event event) {
        if (participantsLimit==event.getParticipantsNumber()) {
            event.setStatus(EventStatus.COMPLETED);
        }
    }

    private void checkParticipantsNumber(int number) {
        if (participantsLimit<=number) {
            throw new EventException(EventError.EVENT_FULL_PARTICIPANTS_NUMBER);
        }
    }

    private void validEmployee(String status) {
        if ("inactive".equals(status)) {
            throw new EventException(EventError.EMPLOYEE_IS_NOT_ACTIVE);
        }
    }

    private Event getEvent(String name) {
        return eventRepository.readByName(name)
                .orElseThrow(() -> new EventException(EventError.EVENT_NOT_FOUND_EXCEPTION));
    }

    private List<String> getEmployeeEmails(Event event) {
        return event.getEmployeeMembers()
                .stream()
                .map(employeeMember -> employeeMember.getEmail())
                .collect(Collectors.toList());
    }
}
