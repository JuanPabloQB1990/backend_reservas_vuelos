package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class TipoVueloDto {

    private Long idTipoVuelo;
    private String nombre;
}
