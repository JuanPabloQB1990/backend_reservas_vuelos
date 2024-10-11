package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class VueloModelDto {

    private Long idVuelo;

    private String codVuelo;

    private String origen;

    private String destino;

    private LocalDateTime fechaPartida;

    private LocalDateTime fechaLlegada;

    private double precio;

    private int asientos;

    private String tipoVuelo;

    private String aerolinea;
}
