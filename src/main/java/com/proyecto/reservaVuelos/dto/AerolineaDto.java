package com.proyecto.reservaVuelos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AerolineaDto {

   private Long idAerolinea;
   private String nombre;
}
