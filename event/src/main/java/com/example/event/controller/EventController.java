package com.example.event.controller;

import com.example.event.model.Event;
import com.example.event.model.dto.Employee;
import com.example.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping
    ResponseEntity<Event>createEvent(@RequestBody @Valid Event event){
        return ResponseEntity.ok(eventService.createEvent(event));
    }
    @GetMapping
    ResponseEntity<List<Event>>readEvents(){
        return ResponseEntity.ok(eventService.readEvents());
    }
    @GetMapping("/{eventName}")
    ResponseEntity<Event>readEvent(@PathVariable String eventName){
        return ResponseEntity.ok(eventService.readEventByName(eventName));
    }
    @GetMapping("/{eventName}/employees")
    ResponseEntity<List<Employee>>readEmployees(@PathVariable String eventName){
        return ResponseEntity.ok(eventService.readEmployees(eventName));
    }
    @PostMapping("/{eventName}/employees/{employeeId}")
    ResponseEntity<Employee>addEmployee(@PathVariable String eventName,@PathVariable Long employeeId){
        return ResponseEntity.ok(eventService.addEmployee(eventName,employeeId));
    }
    @DeleteMapping("/{eventName}")
    ResponseEntity<?>deactivationEvent(@PathVariable String eventName){
        eventService.deactivationEvent(eventName);
        return ResponseEntity.noContent().build();
    }
}
