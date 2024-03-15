package com.udemy.spring.employeeservice.service.impl;

import com.udemy.spring.employeeservice.dto.EmployeeDto;
import com.udemy.spring.employeeservice.entity.Employee;
import com.udemy.spring.employeeservice.exception.EmailAlreadyExistsException;
import com.udemy.spring.employeeservice.exception.ResourceNotFoundException;
import com.udemy.spring.employeeservice.mapper.AutoEmployeeMapper;
import com.udemy.spring.employeeservice.repository.EmployeeRepository;
import com.udemy.spring.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        //Convert UserDto into User JPA Entity
        //User user = UserMapper.mapToUser(userDto);
//        User user = modelMapper.map(userDto, User.class); //ORIGINAL MAPPING METHOD

        Optional<Employee> optionalUser = employeeRepository.findByEmail(employeeDto.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists for User");
        }

        //Mapstruct method
        Employee employee = AutoEmployeeMapper.MAPPER.mapToEmployee(employeeDto);

        Employee savedEmployee = employeeRepository.save(employee);

        //Convert User JPA entity to UserDto
//        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
//        UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class); //ORI MAPPING METHOD

        //Mapstruct method
        EmployeeDto savedEmployeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(savedEmployee);


        return savedEmployeeDto;
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
//        Optional<User> optionalUser = userRepository.findById(userId); //w/o catching exception
//        User user = optionalUser.get();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId)
        );

//        return UserMapper.mapToUserDto(user);

        //original mapping method
//        return modelMapper.map(user, UserDto.class);

        //Mapstruct method
//        return AutoUserMapper.MAPPER.mapToUserDto(optionalUser.get());
        return AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
//        return users.stream().map(UserMapper::mapToUserDto)
//                .collect(Collectors.toList());

        //ori mapping method
//        return users.stream().map((user) -> modelMapper.map(user, UserDto.class))
//                .collect(Collectors.toList());

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
//        return UserMapper.mapToUserDto(updatedUser);

        //Ori mapping method
//        return modelMapper.map(updatedUser, UserDto.class);

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
