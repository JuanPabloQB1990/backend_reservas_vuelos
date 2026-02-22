package com.proyecto.reservaVuelos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@Entity
@Table(name = "aeropuertos")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AeropuertoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAeropuerto;
    private String codigo;
    private String ciudad;
    private String nombreAeropuerto;
    private String pais;

    public AeropuertoModel(String codigo, String ciudad,String nombreAeropuerto, String pais) {
        this.codigo = codigo;
        this.ciudad = ciudad;
        this.pais = pais;
        this.nombreAeropuerto = nombreAeropuerto;
    }
}
