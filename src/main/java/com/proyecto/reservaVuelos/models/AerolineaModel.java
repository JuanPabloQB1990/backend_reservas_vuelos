package com.proyecto.reservaVuelos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "aerolineas")
@AllArgsConstructor
@NoArgsConstructor
public class AerolineaModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idAerolinea;

    @NotEmpty(message = "el nombre de la aerolinea es requerido")
    private String nombre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aerolinea", cascade = CascadeType.ALL)
    private List<VueloModel> vuelos = new ArrayList<>();

    public AerolineaModel(Long idAerolinea, String nombre) {
        this.idAerolinea = idAerolinea;
        this.nombre = nombre;
    }
}
