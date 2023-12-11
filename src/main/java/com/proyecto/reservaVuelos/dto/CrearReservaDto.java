package com.proyecto.reservaVuelos.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrearReservaDto {

    private Long idVuelo1;

    //@Nullable
    private Long idVuelo2;

    //@Nullable
    private Long idVuelo3;


    private LocalDateTime fechaReserva;

    @NotNull(message = "el numero de asientos a reservar son requeridos")
    private int asientos;

    @NotNull(message = "el id del cliente es requerido")
    private Long idCliente;

    public CrearReservaDto(Long idVuelo1, int asientos, Long idCliente) {
        this.idVuelo1 = idVuelo1;
        this.asientos = asientos;
        this.idCliente = idCliente;
    }

    public CrearReservaDto(Long idVuelo1, Long idVuelo2, int asientos, Long idCliente) {
        this.idVuelo1 = idVuelo1;
        this.idVuelo2 = idVuelo2;
        this.asientos = asientos;
        this.idCliente = idCliente;
    }

    public CrearReservaDto(Long idVuelo1, Long idVuelo2, Long idVuelo3, int asientos, Long idCliente) {
        this.idVuelo1 = idVuelo1;
        this.idVuelo2 = idVuelo2;
        this.idVuelo3 = idVuelo3;
        this.asientos = asientos;
        this.idCliente = idCliente;
    }

    public CrearReservaDto() {
    }
}
