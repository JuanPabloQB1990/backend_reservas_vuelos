package com.proyecto.reservaVuelos.excepcion;

import org.springframework.http.HttpStatusCode;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String message) {
        super(message);
    }
}
