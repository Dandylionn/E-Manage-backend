package com.udemy.spring.employeeservice.service.impl;

import com.udemy.spring.employeeservice.dto.APIResponseDto;
import com.udemy.spring.employeeservice.dto.DepartmentDto;
import com.udemy.spring.employeeservice.dto.EmployeeDto;
import com.udemy.spring.employeeservice.dto.OrganizationDto;
import com.udemy.spring.employeeservice.entity.Employee;
import com.udemy.spring.employeeservice.exception.EmailAlreadyExistsException;
import com.udemy.spring.employeeservice.exception.ResourceNotFoundException;
import com.udemy.spring.employeeservice.mapper.AutoEmployeeMapper;
import com.udemy.spring.employeeservice.repository.EmployeeRepository;
import com.udemy.spring.employeeservice.service.APIClient;
import com.udemy.spring.employeeservice.service.EmployeeService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

//    private RestTemplate restTemplate;
    private WebClient webClient;
    private APIClient apiClient;

    private ModelMapper modelMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

//    @CircuitBreaker(name="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}",  fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        LOGGER.info("inside getEmployeeById() method");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/"
//                + employee.getDepartmentCode(),
//                DepartmentDto.class);
//        DepartmentDto departmentDto = responseEntity.getBody();

        //webclient method
        DepartmentDto departmentDto = webClient.get().uri("http://localhost:8080/api/departments/"
        + employee.getDepartmentCode())
                .retrieve().bodyToMono(DepartmentDto.class).block();

        OrganizationDto organizationDto = webClient.get().uri("http://localhost:8083/api/organizations/"
                        + employee.getOrganizationCode())
                .retrieve().bodyToMono(OrganizationDto.class).block();

        //apiclient method
//        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());


        //Mapstruct method
        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee((employeeDto));
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

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

    public APIResponseDto getDefaultDepartment(Long employeeId, Exception exception){

        LOGGER.info("inside getDefaultDepartment() method");

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("Default Department");
        departmentDto.setDepartmentCode("Default");
        departmentDto.setDepartmentDescription("Default Description");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setOrganizationName("Default Organization Name");
        organizationDto.setOrganizationDescription("Default Organization Description");
        organizationDto.setOrganizationCode("Default Code");
        organizationDto.setOrganizationCreatedDate(LocalDateTime.now());

        //Mapstruct method
        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee((employeeDto));
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }


}
