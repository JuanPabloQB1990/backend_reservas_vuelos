package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ClienteModelDto {

    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String username;
    private String pais;
    private String ciudad;
    private String direccion;


}
