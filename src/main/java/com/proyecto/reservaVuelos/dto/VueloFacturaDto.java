package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VueloFacturaDto {

    private String codigoVuelo;
    private String origen;
    private String destino;
    private LocalDateTime salida;
    private double precio;

}
