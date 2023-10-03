package com.example.employee.service.EmployeeService;

import com.example.employee.exception.EmployeeException.EmployeeException;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.EmployeeEnums.Status;
import com.example.employee.model.dto.EmployeeDto;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readEmployee_withEmployeeIdNotFound_throwsEmployeeException() {
        //given
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        Employee employee = new Employee();

        //when
        Exception exception = assertThrows(EmployeeException.class, () -> employeeServiceImpl.readEmployee(employee.getId()));

        //then
        assertThat(exception).isInstanceOf(EmployeeException.class);
        assertThat(exception).hasMessageContaining("employee with given id not found !");
    }

    @Test
    void readEmployee_withEmployeeIdFound() {
        //given
        Employee employee = getEmployee();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));

        //when
        EmployeeDto employee1 = employeeServiceImpl.readEmployee(anyLong());

        //then
        assertNotNull(employee1);

    }
    @Test
    void createEmployee_withEmailExists_throwsEmployeeException_EMPLOYEE_EMAIL_ALREADY_EXISTS(){
        //given
        Employee employee = getEmployee();
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        //when
        Exception exception = assertThrows(EmployeeException.class, () -> employeeServiceImpl.createEmployee(employee));

        //then
        assertThat(exception).hasMessage("Employee with given email already exists !");
        verify(employeeRepository,never()).save(employee);
    }
    @Test
    void createEmployee_withCorrectData(){
        //given
        Employee employee = getEmployee();
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any())).thenReturn(employee);

        //when
        EmployeeDto createdEmployee = employeeServiceImpl.createEmployee(employee);

        //then
        assertNotNull(createdEmployee);
    }

    @Test
    void deactivationEmployee_withEmployeeIdFound() {
        //given
        Employee employee = getEmployee();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        //when
        employeeServiceImpl.deactivationEmployee(anyLong());

        //then
        assertEquals(Status.INACTIVE, employee.getStatus());
        verify(employeeRepository).save(any());
    }

    @Test
    void updateEmployee_withEmployeeIdFoundAndEmployeeStatusIsInactive_throwsEmployeeException_EMPLOYEE_STATUS_INACTIVE() {
        //given
        Employee employee = getEmployee();
        employee.setStatus(Status.INACTIVE);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        //when
        Exception exception = assertThrows(EmployeeException.class, () -> employeeServiceImpl.updateEmployee(anyLong(), employee));

        //then
        assertThat(exception).isInstanceOf(EmployeeException.class);
        assertThat(exception).hasMessage("employee status is INACTIVE !");
    }
    private Employee getEmployee(){
        Employee employee = new Employee();
        employee.setId(2000l);
        employee.setFirstName("Joe");
        employee.setLastName("Doe");
        employee.setPosition(Position.EMPLOYEE);
        employee.setEmail("example@gmail.com");
        return employee;
    }

}