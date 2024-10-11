package com.proyecto.reservaVuelos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservacionModelDto {
    private Long idReservacion;
    private String codigoReservacion;
    private List<VueloModelDto> vuelos;
    private LocalDateTime fechaReservacion;
    private int numAsientos;
    private ClienteModelDto cliente;

    public ReservacionModelDto(Long idReservacion, String codigoReservacion, List<VueloModelDto> vuelos, LocalDateTime fechaReservacion, int numAsientos, ClienteModelDto cliente) {
        this.idReservacion = idReservacion;
        this.codigoReservacion = codigoReservacion;
        this.vuelos = vuelos;
        this.fechaReservacion = fechaReservacion;
        this.numAsientos = numAsientos;
        this.cliente = cliente;
    }
}
