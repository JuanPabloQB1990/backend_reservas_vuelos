package com.proyecto.reservaVuelos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "vuelos")
public class VueloModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVuelo;

    @Column(nullable = false, unique = true)
    private String codigoVuelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id", nullable = false)
    private AeropuertoModel origen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private AeropuertoModel destino;

    @Future
    @NotNull
    private LocalDateTime fechaPartida;

    @Future
    @NotNull
    private LocalDateTime fechaLlegada;

    @Min(1)
    private double precio;

    @Min(1)
    private int asientos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoVuelo")
    private TipoVueloModel tipoVuelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAerolinea")
    private AerolineaModel aerolinea;


}