package com.udemy.spring.employeeservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        description = "EmployeeDto Model Information"
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    //Employee first name should not be null or empty
    @Schema(
            description = "Employee First Name"
    )
    @NotEmpty(message = "Employee first name should not be null or empty")
    private String firstName;

    //Employee last name should not be null or empty
    @Schema(
            description = "Employee Last Name"
    )
    @NotEmpty(message = "Employee last name should not be null or empty")
    private String lastName;

    //Email should be valid
    //Employee email should not be null or
    @Schema(
            description = "Employee Email Address"
    )
    @Email(message = "Email Address should be valid")
    @NotEmpty(message = "Employee email should not be null or empty")
    private String email;
    @NotEmpty(message = "Employee Department Code should not be null or empty")
    private String departmentCode;
}
