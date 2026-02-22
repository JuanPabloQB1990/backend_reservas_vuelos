package com.proyecto.reservaVuelos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vuelos")
@Data
public class VueloModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idVuelo;

    private String codigoVuelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id")
    private AeropuertoModel origenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id")
    private AeropuertoModel destinoId;

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

    public void setOrigen(AeropuertoModel aeropuertoOrigen) {
        this.origenId = aeropuertoOrigen;
    }

    public void setDestino(AeropuertoModel aeropuertoDestino) {
        this.destinoId = aeropuertoDestino;
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
