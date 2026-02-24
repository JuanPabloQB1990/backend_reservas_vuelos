package com.proyecto.reservaVuelos.dto;

import com.proyecto.reservaVuelos.models.ReservacionModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModelDto {

    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String username;
    private String rol;
    private String pais;
    private String ciudad;
    private String direccion;
    private List<ReservacionModel> reservaciones;

}





