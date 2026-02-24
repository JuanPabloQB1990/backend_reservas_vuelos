package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.models.ReservacionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<ReservacionModel, Long> {
    @Query("""
            SELECT r FROM ReservacionModel r
            JOIN FETCH r.vuelos v
            JOIN FETCH r.cliente c
            WHERE c.idCliente = :idCliente
            """)
    List<ReservacionModel> obtenerReservacionesCliente(Long idCliente);

}

