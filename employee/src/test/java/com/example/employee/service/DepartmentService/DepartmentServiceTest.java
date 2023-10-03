package com.example.employee.service.DepartmentService;

import com.example.employee.exception.DepartmentException.DepartmentException;
import com.example.employee.exception.EmployeeException.EmployeeException;
import com.example.employee.model.Department;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.dto.DepartmentEmployeeDto;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployee_withEmployeeDepartmentExists_throwsEmployeeException_EMPLOYEE_ALREADY_EXISTS_IN_SOME_DEPARTMENT() {
        //given
        Employee employee = getEmployee();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));

        //when
        Exception exception = assertThrows(EmployeeException.class, () -> departmentService.addEmployee(any(), employee.getId()));

        //then
        assertThat(exception).hasMessage("Employee with given id already exists in some department !");
        verify(employeeRepository, never()).save(employee);

    }

    @Test
    void addEmployee_withDepartmentIdNotFound_throwsDepartmentException() {
        //given
        Employee employee = getEmployee();
        employee.setDepartment(null);
        when(departmentRepository.findById(any())).thenReturn(Optional.empty());
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));

        //when + then
        Exception exception = assertThrows(DepartmentException.class, () -> departmentService.addEmployee(employee.getId(), any()));

        assertThat(exception).hasMessage("Department with given id not found !");
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void addEmployee_withCorrectData(){
        //given
        Employee employee = getEmployee();
        employee.setDepartment(null);
        Department department = getDepartment();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department));

        //when
        DepartmentEmployeeDto departmentEmployeeDto = departmentService.addEmployee(department.getId(), employee.getId());

        //then
        assertNotNull(departmentEmployeeDto);
    }
    @Test
    void deleteEmployeeFromDepartment_withCorrectData(){
        //given
        Department department = getDepartment();
        Employee employee = getEmployee();
        department.getEmployees().add(employee);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department));

        //when
        departmentService.deleteEmployee(department.getId(),employee.getId());

        //then
        assertTrue(department.getEmployees().isEmpty());
    }

    private Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(2000l);
        employee.setFirstName("Joe");
        employee.setLastName("Doe");
        employee.setPosition(Position.EMPLOYEE);
        employee.setEmail("example@gmail.com");
        employee.setDepartment(new Department());
        return employee;
    }
    private Department getDepartment(){
        Department department = new Department();
        department.setId(1l);
        department.setName("Example");
        department.setDescription("Example");
        return department;
    }


}