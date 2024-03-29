package com.proyecto.reservaVuelos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "tipo_vuelos")
@AllArgsConstructor
@NoArgsConstructor
public class TipoVueloModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idTipoVuelo;

    @Column
    @NotEmpty(message = "el nombre del tipo de vuelo es requerido")
    private String nombre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoVuelo", cascade = CascadeType.ALL)
    private List<VueloModel> vuelos = new ArrayList<>();

    public TipoVueloModel(Long idTipoVuelo, String nombre) {
        this.idTipoVuelo = idTipoVuelo;
        this.nombre = nombre;
    }
}
