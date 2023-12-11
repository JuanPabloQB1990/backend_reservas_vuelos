package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.VueloModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FacturaReservacionDto {

    private String message;
    private String codigo;
    private LocalDateTime fechaReserva;
    private List<VueloModel> vuelos ;
    private int asientos;
    private double total_a_pagar;

}
