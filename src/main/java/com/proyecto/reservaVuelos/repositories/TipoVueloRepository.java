package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.TipoVueloModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoVueloRepository extends JpaRepository<TipoVueloModel, Long> {
}
