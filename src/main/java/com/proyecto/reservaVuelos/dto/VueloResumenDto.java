package com.proyecto.reservaVuelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Información básica de un vuelo reservado")
public class VueloResumenDto {

    @Schema(description = "Código del vuelo", example = "AV2031")
    private String codigoVuelo;

    @Schema(description = "Aeropuerto de origen", example = "Bogotá - El Dorado")
    private String origen;

    @Schema(description = "Aeropuerto de destino", example = "Medellín - José María Córdova")
    private String destino;

    @Schema(description = "Fecha y hora de salida del vuelo")
    private LocalDateTime salida;
}
