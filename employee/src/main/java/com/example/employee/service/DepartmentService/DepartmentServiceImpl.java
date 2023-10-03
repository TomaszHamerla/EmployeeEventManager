package com.example.employee.service.DepartmentService;

import com.example.employee.exception.DepartmentException.DepartmentError;
import com.example.employee.exception.DepartmentException.DepartmentException;
import com.example.employee.exception.EmployeeException.EmployeeError;
import com.example.employee.exception.EmployeeException.EmployeeException;
import com.example.employee.model.Department;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Status;
import com.example.employee.model.dto.DepartmentDto;
import com.example.employee.model.dto.DepartmentEmployeeDto;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.operations.EmployeeValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService, EmployeeValidations {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void deleteEmployee(Long departmentId, Long employeeId) {
        Department departmentFromDb = getDepartment(departmentId);
        Employee employeeFromDb = getEmployee(employeeId);
        employeeFromDb.setDepartment(null);
        departmentFromDb.getEmployees()
                .remove(employeeFromDb);
        employeeRepository.save(employeeFromDb);
        departmentRepository.save(departmentFromDb);
    }

    @Override
    public DepartmentDto createDepartment(Department department) {
        return getDepartmentDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDto readDepartment(Long departmentId) {
        Department department = getDepartment(departmentId);
        return getDepartmentDto(department);
    }

    @Override
    public List<DepartmentDto> readDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::getDepartmentDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentEmployeeDto addEmployee(Long departmentId, Long employeeId) {
        Employee employeeFromDb = getEmployee(employeeId);
        validEmployee(employeeFromDb);
        Department departmentFromDb = getDepartment(departmentId);
        departmentFromDb.getEmployees()
                .add(employeeFromDb);
        employeeFromDb.setDepartment(departmentFromDb);
        employeeRepository.save(employeeFromDb);
        departmentRepository.save(departmentFromDb);
        return getDepartmentEmployeeDto(employeeFromDb);

    }

    private void validEmployee(Employee employeeFromDb) {
        if (employeeFromDb.getDepartment() != null) {
            throw new EmployeeException(EmployeeError.EMPLOYEE_ALREADY_EXISTS_IN_SOME_DEPARTMENT);
        }
        validStatus(employeeFromDb.getStatus());

    }

    @Override
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeException(EmployeeError.EMPLOYEE_ID_NOT_FOUND));
    }

    private Department getDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(DepartmentError.DEPARTMENT_ID_NOT_FOUND));
    }

    private DepartmentDto getDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .employees(department.getEmployees().stream().map(employee ->
                                getDepartmentEmployeeDto(employee))
                        .collect(Collectors.toList())).build();
    }

    private DepartmentEmployeeDto getDepartmentEmployeeDto(Employee employeeFromDb) {
        return DepartmentEmployeeDto.builder()
                .id(employeeFromDb.getId())
                .lastName(employeeFromDb.getLastName())
                .firstName(employeeFromDb.getFirstName())
                .position(employeeFromDb.getPosition().toString().toLowerCase())
                .build();
    }
    @Override
    public void validStatus(Status status) {
        if (Status.INACTIVE.equals(status)) {
            throw new EmployeeException(EmployeeError.EMPLOYEE_STATUS_INACTIVE);
        }
    }
}
