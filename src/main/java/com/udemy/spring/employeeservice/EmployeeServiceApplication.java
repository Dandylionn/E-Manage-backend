package com.udemy.spring.employeeservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "E-Manage",
				description = "Spring Boot Web App",
				version = "v1.0",
				contact = @Contact(
						name = "kzh",
						email = "test@gmail.com",
						url = "dummy.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "dummy/com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot User Management Web App",
				url = "dummy2.com"
		)
)
public class EmployeeServiceApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
