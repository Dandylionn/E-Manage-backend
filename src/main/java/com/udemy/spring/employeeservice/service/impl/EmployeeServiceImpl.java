package com.udemy.spring.employeeservice.service.impl;

import com.udemy.spring.employeeservice.dto.APIResponseDto;
import com.udemy.spring.employeeservice.dto.DepartmentDto;
import com.udemy.spring.employeeservice.dto.EmployeeDto;
import com.udemy.spring.employeeservice.entity.Employee;
import com.udemy.spring.employeeservice.exception.EmailAlreadyExistsException;
import com.udemy.spring.employeeservice.exception.ResourceNotFoundException;
import com.udemy.spring.employeeservice.mapper.AutoEmployeeMapper;
import com.udemy.spring.employeeservice.repository.EmployeeRepository;
import com.udemy.spring.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private RestTemplate restTemplate;

    private ModelMapper modelMapper;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        //Convert UserDto into User JPA Entity
        Optional<Employee> optionalUser = employeeRepository.findByEmail(employeeDto.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists for User");
        }

        //Mapstruct method
        Employee employee = AutoEmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        //Convert User JPA entity to UserDto
        //Mapstruct method
        EmployeeDto savedEmployeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(savedEmployee);


        return savedEmployeeDto;
    }

    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );

        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/"
                + employee.getDepartmentCode(),
                DepartmentDto.class);

        DepartmentDto departmentDto = responseEntity.getBody();

        //Mapstruct method
        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployeeDto((employeeDto));
        apiResponseDto.setDepartmentDto(departmentDto);

        return apiResponseDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        //Mapstruct method
        return employees.stream().map((employee) -> AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employee) {
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employee.getId())
        );
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        //Mapstruct method
        return AutoEmployeeMapper.MAPPER.mapToEmployeeDto(updatedEmployee);
    }


    @Override
    public void deleteEmployee(Long employeeId) {
        Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );
        employeeRepository.deleteById(employeeId);
    }


}
