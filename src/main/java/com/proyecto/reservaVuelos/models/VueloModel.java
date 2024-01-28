package com.proyecto.reservaVuelos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vuelos")
public class VueloModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idVuelo;

    private String codigoVuelo;

    @NotEmpty(message = "el origen del vuelo es obligatorio")
    private String origen;

    @NotEmpty(message = "el destino del vuelo es obligatorio")
    private String destino;

    @Future
    @NotNull(message = "la fecha de partida del vuelo es obligatorio")
    private LocalDateTime fechaPartida;

    @Future
    @NotNull(message = "la fecha de llegada del vuelo es obligatorio")
    private LocalDateTime fechaLlegada;

    @Min(value = 1, message = "el precio del vuelo debe ser saldo positivo")
    private double precio;

    @Min(value = 1, message = "los asientos deben ser mayor a 0")
    private int asientos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipoVuelo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TipoVueloModel tipoVuelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAerolinea")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AerolineaModel aerolinea;

    public void setIdVuelo(Long idVuelo) {
        this.idVuelo = idVuelo;
    }

    public void setCodigoVuelo(String codigoVuelo) {
        this.codigoVuelo = codigoVuelo;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setFechaPartida(LocalDateTime fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setAsientos(int asientos) {
        this.asientos = asientos;
    }

    public void setTipoVuelo(TipoVueloModel tipoVuelo) {
        this.tipoVuelo = tipoVuelo;
    }

    public void setAerolinea(AerolineaModel aerolinea) {
        this.aerolinea = aerolinea;
    }
}
