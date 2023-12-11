package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.RolModel;
import lombok.Data;

@Data
public class AuthRespuestaDto {
    private String accesToken;
    private RolModel rol;
    private String tokenType = "Bearer ";

    public AuthRespuestaDto(String accesToken, RolModel rol) {
        this.accesToken = accesToken;
        this.rol = rol;
    }
}
