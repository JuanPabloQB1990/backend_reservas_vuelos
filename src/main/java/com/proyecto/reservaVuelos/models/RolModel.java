package com.proyecto.reservaVuelos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


public enum RolModel {

    ADMIN, EMPLEADO, CLIENTE
}
