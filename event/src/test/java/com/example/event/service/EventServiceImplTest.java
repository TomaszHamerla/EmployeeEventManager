package com.example.event.service;

import com.example.event.exception.EventException;
import com.example.event.model.EmployeeMember;
import com.example.event.model.Event;
import com.example.event.model.dto.Employee;
import com.example.event.repository.EmployeeServiceClient;
import com.example.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @InjectMocks
    private EventServiceImpl eventService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent_withInvalidStartDateAndEndDate_throwsEventExceptionEVENT_DATE_CONFLICT(){
        //given
        Event event = getEvent();
        event.setStartDate(event.getEndDate().plusDays(1));

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.createEvent(event));

        //then
        assertThat(exception).hasMessage("EndDate can not be earlier then startDate !");
        verify(eventRepository,never()).save(event);
    }
    @Test
    void createEvent_withValidData_and_duplicateEventName_throwsEventExceptionEVENT_DUPLICATE_NAME(){
        //given
        Event event = getEvent();
        when(eventRepository.existsByName(anyString())).thenReturn(true);

        //when
       Exception exception = assertThrows(EventException.class, () -> eventService.createEvent(event));

       //then
        assertThat(exception).hasMessage("Can not create event with this name !");
        verify(eventRepository,never()).save(event);
    }
    @Test
    void createEvent_withValidData_and_noDuplicateEventName(){
        //given
        Event event = getEvent();
        when(eventRepository.existsByName(anyString())).thenReturn(false);
        when(eventRepository.save(any())).thenReturn(event);

        //when
        Event createdEvent = eventService.createEvent(event);

        //then
        assertNotNull(createdEvent);
        assertEquals(event,createdEvent);
    }
    @Test
    void readEventByName_withNameNotFound_throwsEventExceptionEVENT_NOT_FOUND_EXCEPTION(){
        //given
        Event event = getEvent();
        when(eventRepository.readByName(anyString())).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(EventException.class, () -> eventService.readEventByName(event.getName()));

        //then
        assertThat(exception).hasMessage("Event with given name not found !");
    }
    @Test
    void readEventByName_withNameFound_returnsEventObj(){
        //given
        Event event = getEvent();
        when(eventRepository.readByName(anyString())).thenReturn(Optional.of(event));

        //when
        Event eventFromDb = eventService.readEventByName(event.getName());

        //then
        assertNotNull(eventFromDb);
    }
    //FIXME
    @Test
    void readEmployees_withEventNameFound_returnsListOfEmployees(){
        //given
        Event event = getEvent();
        when(eventRepository.readByName(any())).thenReturn(Optional.of(event));
        Employee employee = new Employee();
        employee.setFirstName("joe");
        employee.setLastName("doe");
        employee.setEmail("example@gmail.com");
        List<Employee>employees=List.of(employee);
        event.getEmployeeMembers().add(new EmployeeMember("example@gmail.com"));
        when(employeeServiceClient.getEmployeesByEmails(any())).thenReturn(employees);

        //when
        List<Employee> employeesFromEvent = eventService.readEmployees(event.getName());

        //then
        assertEquals(employee.getEmail(),employees.get(0));

    }

    private Event getEvent() {
        Event event = new Event();
        event.setId("123");
        event.setName("example");
        event.setDescription("example");
        event.setStartDate(LocalDateTime.now());
        event.setEndDate(LocalDateTime.now().plusDays(1));
        return event;
    }

}