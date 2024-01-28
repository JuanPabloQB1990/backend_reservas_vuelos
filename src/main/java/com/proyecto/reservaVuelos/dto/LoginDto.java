package com.proyecto.reservaVuelos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {

    @NotEmpty(message = "el username es requerido")
    @NotBlank(message = "el username no se permiten espacios")
    private String username;

    @NotEmpty(message = "el password es requerido")
    @NotBlank(message = "el password no se permiten espacios")
    private String password;
}
