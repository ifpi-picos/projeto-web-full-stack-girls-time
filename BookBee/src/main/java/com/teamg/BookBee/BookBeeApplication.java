package com.teamg.BookBee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title= "BookBee", version = "1.1"))
public class BookBeeApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookBeeApplication.class);
	public static void main(String[] args) {
		LOGGER.info("Iniciando a aplicacao BookBee...");
		SpringApplication.run(BookBeeApplication.class, args);
		LOGGER.info("Aplicacao Iniciada.");
	}

}
