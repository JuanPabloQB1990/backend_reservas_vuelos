package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.AeropuertoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AeropuertoRepository extends JpaRepository<AeropuertoModel, Long> {
}
