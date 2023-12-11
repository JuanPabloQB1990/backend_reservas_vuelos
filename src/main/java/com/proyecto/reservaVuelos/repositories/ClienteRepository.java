package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    Optional<ClienteModel> findByCorreo(String correo);
    Optional<ClienteModel> findByUsername(String username);

    Boolean existsByUsername(String username);



}
