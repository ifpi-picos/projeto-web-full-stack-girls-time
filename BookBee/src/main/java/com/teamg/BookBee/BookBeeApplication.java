package com.teamg.BookBee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title= "BookBee", version = "1"))
public class BookBeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookBeeApplication.class, args);
	}

}
