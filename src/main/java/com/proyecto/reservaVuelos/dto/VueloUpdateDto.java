package com.proyecto.reservaVuelos.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class VueloUpdateDto {
    private String codigoVuelo;
    private Long origenId;
    private Long destinoId;
    private LocalDateTime fechaPartida;
    private LocalDateTime fechaLlegada;
    private Double precio;
    private Integer asientos;
    private Long tipoVueloId;
    private Long aerolineaId;
}
