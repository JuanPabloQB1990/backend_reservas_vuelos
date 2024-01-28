package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.RolModel;
import lombok.Data;

@Data
public class AuthRespuestaDto {
    private String accesToken;
    private RolModel rol;
    private Long idCliente;
    private String tokenType = "Bearer ";

    public AuthRespuestaDto(String accesToken, RolModel rol, Long idCliente) {
        this.accesToken = accesToken;
        this.rol = rol;
        this.idCliente = idCliente;
    }
}
