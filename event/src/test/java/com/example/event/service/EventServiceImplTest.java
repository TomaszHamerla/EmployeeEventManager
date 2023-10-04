package com.example.event.service;

import com.example.event.exception.EventException;
import com.example.event.model.Event;
import com.example.event.repository.EmployeeServiceClient;
import com.example.event.repository.EventRepository;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

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