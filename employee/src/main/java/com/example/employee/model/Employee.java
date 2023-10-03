package com.example.employee.model;

import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.EmployeeEnums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SequenceGenerator(allocationSize = 1, initialValue = 2000, name = "seq")
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    @NotBlank(message = "firstName can not be blank !")
    private String firstName;
    @NotBlank(message = "lastName can not be blank !")
    private String lastName;
    @Email
    @Column(unique = true)
    private String email;

    private LocalDateTime hireDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Position position;                        //  EMPLOYEE,MANAGER
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;            //init employee with ACTIVE status
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

}
