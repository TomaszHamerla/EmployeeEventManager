package com.example.event.service;

import com.example.event.exception.EventException;
import com.example.event.model.EmployeeMember;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.model.dto.Employee;
import com.example.event.repository.EmployeeServiceClient;
import com.example.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EmployeeServiceClient employeeServiceClient;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent_withInvalidStartDateAndEndDate_throwsEventExceptionEVENT_DATE_CONFLICT() {
        //given
        Event event = getEvent();
        event.setStartDate(event.getEndDate().plusDays(1));

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.createEvent(event));

        //then
        assertThat(exception).hasMessage("EndDate can not be earlier then startDate !");
        verify(eventRepository, never()).save(event);
    }

    @Test
    void createEvent_withValidData_and_duplicateEventName_throwsEventExceptionEVENT_DUPLICATE_NAME() {
        //given
        Event event = getEvent();
        when(eventRepository.existsByName(anyString())).thenReturn(true);

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.createEvent(event));

        //then
        assertThat(exception).hasMessage("Can not create event with this name !");
        verify(eventRepository, never()).save(event);
    }

    @Test
    void createEvent_withValidData_and_noDuplicateEventName() {
        //given
        Event event = getEvent();
        when(eventRepository.existsByName(anyString())).thenReturn(false);
        when(eventRepository.save(any())).thenReturn(event);

        //when
        Event createdEvent = eventService.createEvent(event);

        //then
        assertNotNull(createdEvent);
        assertEquals(event, createdEvent);
    }

    @Test
    void readEventByName_withNameNotFound_throwsEventExceptionEVENT_NOT_FOUND_EXCEPTION() {
        //given
        Event event = getEvent();
        when(eventRepository.readByName(anyString())).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.readEventByName(event.getName()));

        //then
        assertThat(exception).hasMessage("Event with given name not found !");
    }

    @Test
    void readEventByName_withNameFound_returnsEventObj() {
        //given
        Event event = getEvent();
        when(eventRepository.readByName(anyString())).thenReturn(Optional.of(event));

        //when
        Event eventFromDb = eventService.readEventByName(event.getName());

        //then
        assertNotNull(eventFromDb);
    }

    @Test
    void readEmployees_withEventNameFound_returnsListOfEmployees() {
        //given
        Event event = getEvent();
        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));
        Employee employee = getEmployee();
        List<Employee> employees = List.of(employee);
        event.getEmployeeMembers().add(new EmployeeMember("example@gmail.com"));
        when(employeeServiceClient.getEmployeesByEmails(any())).thenReturn(employees);

        //when
        List<Employee> employeesFromEvent = eventService.readEmployees(event.getName());

        //then
        assertEquals(employee.getEmail(), employeesFromEvent.get(0).getEmail());

    }

    @Test
    void addEmployee_withEventNameFound_and_StatusEqualCancel_throwsEventExceptionEVENT_NOT_AVAILABLE_STATUS() {
        //given
        Event event = getEvent();
        event.setStatus(EventStatus.CANCEL);
        when(eventRepository.readByName(anyString())).thenReturn(Optional.of(event));

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.addEmployee(event.getName(), any()));

        //then
        assertThat(exception).hasMessage("Event with given name has not active status !");

    }

    @Test
    void addEmployee_withEventNameFound_and_StatusIsIN_PROGRESS_or_COMPLETED_and_participantsNumberEqual_5_throwsEventExceptionEVENT_FULL_PARTICIPANTS_NUMBER() {
        //given
        Event event = getEvent();
        event.setParticipantsNumber(5);
        when(eventRepository.readByName(anyString())).thenReturn(Optional.of(event));

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.addEmployee(event.getName(), any()));

        //then
        assertThat(exception).hasMessage("Can not add another employee to this event because limit is full !");
    }

    @Test
    void addEmployee_withStatusIsInactive() {
        //given
        Event event = getEvent();
        when(eventRepository.readByName(anyString())).thenReturn(Optional.of(event));
        Employee employee = getEmployee();
        employee.setStatus("inactive");
        when(employeeServiceClient.getEmployee(any())).thenReturn(employee);

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.addEmployee(event.getName(), anyLong()));

        //then
        assertThat(exception).hasMessage("Employee with given id is not active !");

    }

    @Test
    void addEmployee_withCorrectData_returnsEmployee() {
        //when
        Event event = getEvent();       //init with participantsNumber = 0
        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));
        Employee employee = getEmployee();
        when(employeeServiceClient.getEmployee(any())).thenReturn(employee);

        //when
        Employee createdEmployee = eventService.addEmployee(event.getName(), any());

        //then
        assertEquals(employee, createdEmployee);
        assertEquals(1, event.getParticipantsNumber());
    }

    @Test
    void deactivationEvent_withEventNameFound_shouldSetEventStatusTo_cancel() {
        //given
        Event event = getEvent();   //init event with status IN_PROGRESS
        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));

        //when
        eventService.deactivationEvent(event.getName());

        //then
        assertEquals(EventStatus.CANCEL, event.getStatus());
    }

    @Test
    void removeEmployee_withEventNameFound() {
        //given
        Event event = getEvent();
        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));
        Employee employee = getEmployee();
        event.getEmployeeMembers().add(new EmployeeMember(employee.getEmail()));
        event.setParticipantsNumber(1);

        //when
        eventService.removeEmployee(event.getName(), any());

        //then
        assertEquals(0, event.getParticipantsNumber());
    }
//TODO
//    @Test
//    void finishEventEnrollment_shouldSendNotificationInConsole_and_setCompletedStatus() {
//        //given
//
//        Event event = getEvent();
//        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));
//        Employee employee = getEmployee();
//        event.getEmployeeMembers().add(new EmployeeMember(employee.getEmail()));
//        when(rabbitTemplate.convertSendAndReceive(any())).thenCallRealMethod();
//
//        //when
//        eventService.finishEventEnrollment(event.getName());
//
//        //then
//        assertEquals(EventStatus.COMPLETED, event.getStatus());
//    }

    private Event getEvent() {
        Event event = new Event();
        event.setId("123");
        event.setName("example");
        event.setDescription("example");
        event.setStartDate(LocalDateTime.now());
        event.setEndDate(LocalDateTime.now().plusDays(1));
        return event;
    }

    private Employee getEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("joe");
        employee.setLastName("doe");
        employee.setEmail("example@gmail.com");
        return employee;
    }

}