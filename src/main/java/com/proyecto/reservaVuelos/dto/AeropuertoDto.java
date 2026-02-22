package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AeropuertoDto {

    private String codigo;
    private String ciudad;
    private String pais;
    private String nombreAeropuerto;
}
