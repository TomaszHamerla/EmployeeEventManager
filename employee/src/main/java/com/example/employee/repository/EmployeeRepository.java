package com.example.employee.repository;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee>findAllByPosition(Position position);
    boolean existsByEmail(String email);
    List<Employee> findByEmailIn(List<String>emails);
}
