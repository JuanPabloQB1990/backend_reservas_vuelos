package com.proyecto.reservaVuelos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ReservaVuelosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservaVuelosApplication.class, args);
	}

}
