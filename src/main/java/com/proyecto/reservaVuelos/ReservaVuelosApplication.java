package com.proyecto.reservaVuelos;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@SecurityScheme(name = "BearerJWT", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT", description = "token para la api")
public class ReservaVuelosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservaVuelosApplication.class, args);
	}

}
