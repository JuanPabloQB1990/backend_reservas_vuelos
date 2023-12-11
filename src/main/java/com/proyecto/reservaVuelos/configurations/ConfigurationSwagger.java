package com.proyecto.reservaVuelos.configurations;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.VueloModel;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigurationSwagger {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Api Rest Full para gestion de reservas de Vuelos.")
                        .description("Esta API REST permite para el cliente buscar vuelos ya sean directos o con escalas.")
                        .contact(new Contact()
                                .name("Juan Quintero")
                                .email("juanpabloqb1990@gmail.com")
                                .url("https://www.linkedin.com/in/juanpabloqb/"))
                        .version("1.0"));
    }



}
