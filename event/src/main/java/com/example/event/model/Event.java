package com.example.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@Getter
@Setter
public class Event {
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status=EventStatus.IN_PROGRESS;
    @Max(5)
    @JsonIgnore
    private int participantsLimit;
    private int participantsNumber=0;
    private Set<EmployeeMember> employeeMembers = new HashSet<>();

}
