package com.udemy.spring.employeeservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    private Long id;
    @NotEmpty(message = "Organization name should not be null or empty")
    private String organizationName;
    private String organizationDescription;
    @NotEmpty(message = "Organization Code should not be null or empty")
    private String organizationCode;
    @CreationTimestamp
    private LocalDateTime organizationCreatedDate;
}
