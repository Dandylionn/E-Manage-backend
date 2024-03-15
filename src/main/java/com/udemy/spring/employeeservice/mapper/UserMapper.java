package com.udemy.spring.employeeservice.mapper;

import com.udemy.spring.employeeservice.dto.EmployeeDto;
import com.udemy.spring.employeeservice.entity.Employee;

public class UserMapper {
    public static EmployeeDto mapToUserDto(Employee employee){
        //Convert User JPA entity into UserDto
        EmployeeDto employeeDto = new EmployeeDto(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail()
        );
        return employeeDto;
    }

    //Convert UserDto into User JPA Entity
    public static Employee mapToUser(EmployeeDto employeeDto){
        Employee employee = new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail()
        );
        return employee;
    }
}
