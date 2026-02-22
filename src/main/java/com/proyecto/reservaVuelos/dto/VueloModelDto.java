package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.VueloModel;
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

    private AeropuertoDto origen;
    private AeropuertoDto destino;

    private LocalDateTime fechaPartida;
    private LocalDateTime fechaLlegada;

    private double precio;
    private int asientos;

    private TipoVueloDto tipoVuelo;
    private AerolineaDto aerolinea;
}
