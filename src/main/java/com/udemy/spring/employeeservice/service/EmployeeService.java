package com.udemy.spring.employeeservice.service;

import com.udemy.spring.employeeservice.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService
{
    EmployeeDto createEmployee(EmployeeDto user);
    EmployeeDto getEmployeeById(Long employeeId);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto updateEmployee(EmployeeDto employee);

    void deleteEmployee(Long employeeId);
}
