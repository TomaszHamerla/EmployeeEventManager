package com.example.employee.service.EmployeeService;

import com.example.employee.exception.EmployeeException.EmployeeException;
import com.example.employee.exception.EmployeeException.EmployeeError;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeEnums.Position;
import com.example.employee.model.EmployeeEnums.Status;
import com.example.employee.model.dto.EmployeeDto;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.operations.EmployeeValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService, EmployeeValidations {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDto createEmployee(Employee employee) {
        validEmail(employee.getEmail());
        Employee saveEmployee = employeeRepository.save(employee);
        return getEmployeeDto(saveEmployee);
    }


    @Override
    public EmployeeDto readEmployee(Long employeeID) {
        Employee employee = getEmployee(employeeID);
        return getEmployeeDto(employee);

    }

    @Override
    public List<EmployeeDto> readEmployees(Position position) {
        if (position != null) {
            return employeeRepository.findAllByPosition(position)
                    .stream().map(employee -> getEmployeeDto(employee))
                    .collect(Collectors.toList());
        }
        return employeeRepository.findAll().stream().map(employee -> getEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public void deactivationEmployee(Long employeeId) {
        Employee employeeFromDB = getEmployee(employeeId);
        employeeFromDB.setStatus(Status.INACTIVE);
        employeeRepository.save(employeeFromDB);

    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, Employee employee) {
        Employee employeeFromDb = getEmployee(employeeId);
        validStatus(employeeFromDb.getStatus());
        if (employee.getLastName() != null) {
            employeeFromDb.setLastName(employee.getLastName());
        }
        if (employee.getEmail() != null) {
            employeeFromDb.setEmail(employee.getEmail());
        }
        if (employee.getPosition() != null) {

            employeeFromDb.setPosition(employee.getPosition());
        }
        Employee saveEmployee = employeeRepository.save(employeeFromDb);
        return getEmployeeDto(saveEmployee);
    }


    @Override
    public Employee getEmployee(Long employeeID) {
        return employeeRepository.findById(employeeID)
                .orElseThrow(() -> new EmployeeException(EmployeeError.EMPLOYEE_ID_NOT_FOUND));
    }

    private EmployeeDto getEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .hireDate(employee.getHireDate())
                .position(employee.getPosition().toString().toLowerCase())
                .status(employee.getStatus().toString().toLowerCase())
                .build();
        if (employee.getDepartment() != null) {
            employeeDto.setDepartmentName(employee.getDepartment().getName());
        }
        return employeeDto;

    }

    private void validEmail(String email) {
        if (employeeRepository.existsByEmail(email)) {
            throw new EmployeeException(EmployeeError.EMPLOYEE_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public void validStatus(Status status) {
        if (Status.INACTIVE.equals(status)) {
            throw new EmployeeException(EmployeeError.EMPLOYEE_STATUS_INACTIVE);
        }
    }
}
