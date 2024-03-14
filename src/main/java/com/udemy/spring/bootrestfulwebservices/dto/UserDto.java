package com.udemy.spring.bootrestfulwebservices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        description = "UserDto Model Information"
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    //User first name should not be null or empty
    @Schema(
            description = "User First Name"
    )
    @NotEmpty(message = "User first name should not be null or empty")
    private String firstName;

    //User last name should not be null or empty
    @Schema(
            description = "User Last Name"
    )
    @NotEmpty(message = "User last name should not be null or empty")
    private String lastName;

    //Email should be valid
    //User email should not be null or
    @Schema(
            description = "User Email Address"
    )
    @Email(message = "Email Address should be valid")
    @NotEmpty(message = "User email should not be null or empty")
    private String email;
}
