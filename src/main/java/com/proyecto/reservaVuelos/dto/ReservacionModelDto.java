package com.proyecto.reservaVuelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de una reservación realizada por un cliente")
public class ReservacionModelDto {
    @Schema(description = "ID de la reservación", example = "15")
    private Long idReservacion;

    @Schema(description = "Código único de la reservación", example = "RSV48291")
    private String codigoReservacion;

    @Schema(description = "Fecha en que se realizó la reservación")
    private LocalDateTime fechaReservacion;

    @Schema(description = "Número de asientos reservados", example = "2")
    private Integer asientos;

    @Schema(description = "Costo total pagado por la reservación", example = "350.50")
    private Double total;

    @Schema(description = "Lista de vuelos incluidos en la reservación")
    private List<VueloResumenDto> vuelos;

}
