package com.proyecto.reservaVuelos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "reservaciones")
@AllArgsConstructor
@NoArgsConstructor
public class ReservacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservacion;

    private String codigoReservacion;

    private LocalDateTime fechaReservacion;

    @NotNull(message = "El número de asientos es obligatorio")
    @Min(value = 1, message = "Debe reservar al menos 1 asiento")
    private Integer numeroAsientos;

    @Min(1)
    private double total;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)  // clave FK
    private ClienteModel cliente;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservacion_vuelos",
            joinColumns = @JoinColumn(name = "idReservacion"),
            inverseJoinColumns = @JoinColumn(name = "idVuelo")
    )
    private List<VueloModel> vuelos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EstadoReservacion estado;
}
