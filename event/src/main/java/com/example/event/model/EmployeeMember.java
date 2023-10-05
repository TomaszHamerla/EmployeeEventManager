package com.example.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString        //using @sl4j in eventService to print this in console
public class EmployeeMember {
    private String email;
    private LocalDateTime enrollmentDate;

    public EmployeeMember(String email) {
        this.email = email;
        enrollmentDate=LocalDateTime.now();
    }
}
