package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.VueloModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Factura generada después de realizar una reservación")
public class FacturaReservacionDto {

    @Schema(description = "Mensaje de confirmación")
    private String message;

    @Schema(description = "Código único de la reservación", example = "RSV83921")
    private String codigo;

    @Schema(description = "Fecha en que se realizó la reserva")
    private LocalDateTime fechaReserva;

    @Schema(description = "Lista de vuelos reservados")
    private List<VueloFacturaDto> vuelos;

    @Schema(description = "Cantidad de asientos reservados", example = "2")
    private Integer asientos;

    @Schema(description = "Total a pagar", example = "420.50")
    private Double total_a_pagar;

    private List<String> errors;

}
