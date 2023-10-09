package com.teamg.BookBee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * A Classe princiapl da aplicação BookBee.
 * Ela usa a anotação @SpingBootApplication para iniciar a aplicação Spring Boot
 */
@SpringBootApplication
public class BookBeeApplication {

	/**
	 * O método princiapl que inicia a aplicação Spring Boot.
	 * @param args argumentos da linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookBeeApplication.class, args);
	}

}
