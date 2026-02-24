package com.proyecto.reservaVuelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos necesarios para realizar una reservación")
public class CrearReservaDto {

    @Size(max = 5)
    @Schema(description = "Lista de IDs de vuelos a reservar", example = "[12, 15]")
    @NotEmpty(message = "Debe seleccionar al menos un vuelo")
    private List<Long> vuelosIds;

    @Schema(description = "Número de asientos a reservar", example = "2")
    @Min(value = 1, message = "Debe reservar al menos 1 asiento")
    @NotNull(message = "El número de asientos es obligatorio")
    private Integer numeroAsientos;

    @Schema(description = "ID del cliente que realiza la reservación", example = "3")
    @NotNull(message = "El id del cliente es requerido")
    private Long idCliente;

}
