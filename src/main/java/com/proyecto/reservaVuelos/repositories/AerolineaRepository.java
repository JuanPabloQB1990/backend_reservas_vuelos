package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.AerolineaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AerolineaRepository extends JpaRepository<AerolineaModel, Long> {
}
