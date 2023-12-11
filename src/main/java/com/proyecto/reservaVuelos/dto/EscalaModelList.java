package com.proyecto.reservaVuelos.dto;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EscalaModelList {
    private List<VueloModelDto> vuelos;

    public EscalaModelList(List<VueloModelDto> vuelos) {
        this.vuelos = vuelos;
    }

    public List<VueloModelDto> getVuelos() {
        return vuelos;
    }

    public void setVuelos(List<VueloModelDto> vuelos) {
        this.vuelos = vuelos;
    }
}
