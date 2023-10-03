package com.example.event.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeMember {
    private String email;
    private LocalDateTime enrollmentDate;

    public EmployeeMember(String email) {
        this.email = email;
        enrollmentDate=LocalDateTime.now();
    }
}
