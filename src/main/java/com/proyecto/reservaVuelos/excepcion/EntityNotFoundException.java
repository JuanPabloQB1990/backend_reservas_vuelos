package com.proyecto.reservaVuelos.excepcion;

import org.springframework.http.HttpStatusCode;

public class EntityNotFoundException extends Exception{

    private HttpStatusCode code;

    public EntityNotFoundException(String message, HttpStatusCode code) {
        super(message);
        this.code = code;
    }
}
