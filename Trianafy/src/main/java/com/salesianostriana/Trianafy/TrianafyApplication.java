package com.salesianostriana.Trianafy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(description = "Proyecto 01 de AD y PSP Trianafy," +
		" Alejandro Martín, Guillermo De la cruz, Antonio Montero",
		version = "1.0",
		title = "Trianafy")
)

public class TrianafyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrianafyApplication.class, args);
	}

}
