package com.proyecto.reservaVuelos.repositories;

import com.proyecto.reservaVuelos.models.VueloModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VueloRepository extends JpaRepository<VueloModel, Long> {

    boolean existsBy();

    List<VueloModel> findByFechaPartidaBefore(LocalDateTime fecha);

    @Query("""
        SELECT v FROM VueloModel v
        WHERE v.fechaPartida <= :fechaActual
        """)
    List<VueloModel> vuelosExpirados(LocalDateTime fechaActual);

//    @Query("""
//        SELECT v FROM VueloModel v
//        WHERE LOWER(v.origen_id.ciudad) = LOWER(:origen_id)
//        AND LOWER(v.destino_id.ciudad) = LOWER(:destino_id)
//        AND DATE(v.fechaPartida) = DATE(:fecha)
//    """)
//    List<VueloModel> buscarVuelosDirectos(
//            String origen,
//            String destino,
//            LocalDateTime fecha
//    );

    @Query("""
        SELECT v FROM VueloModel v
        WHERE TRIM(LOWER(v.origenId.ciudad)) = TRIM(LOWER(:origen))
        AND v.fechaPartida >= :inicioDia
        AND v.fechaPartida < :finDia
        AND v.asientos >= :pasajeros
        ORDER BY v.fechaPartida
        """)
    List<VueloModel> buscarPrimerTramo(
            @Param("origen") String origen,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia,
            @Param("pasajeros") int pasajeros
    );

    @Query("""
            SELECT v FROM VueloModel v
            WHERE TRIM(LOWER(v.origenId.ciudad)) = TRIM(LOWER(:ciudad))
            AND v.fechaPartida >= :min
            AND v.fechaPartida <= :max
            AND v.asientos >= :pasajeros
            ORDER BY v.fechaPartida
            """)
    List<VueloModel> buscarConexionesValidas(
            @Param("ciudad") String ciudad,
            @Param("min") LocalDateTime min,
            @Param("max") LocalDateTime max,
            @Param("pasajeros") int pasajeros
    );

    @Query("""
            SELECT v FROM VueloModel v
            WHERE LOWER(v.origenId.ciudad) = LOWER(:ciudad)
            AND v.fechaPartida > :fecha
            ORDER BY v.fechaPartida ASC
            """)
    List<VueloModel> buscarSiguientesTramos(
            @Param("ciudad") String ciudad,
            @Param("fecha") LocalDateTime fecha
    );

    @Transactional
    @Modifying
    @Query(value = "{call update_vuelo(:idVuelo, :origenId, :destinoId, :fechaPartida, :fechaLlegada, :precio, :asientos, :idTipoVuelo, :idAerolinea)}", nativeQuery = true)
    void actualizarVuelo(@Param("idVuelo")Long idVuelo,
                         @Param("origenId")Long origenId,
                         @Param("destinoId")Long destinoId,
                         @Param("fechaPartida") LocalDateTime fechaPartida,
                         @Param("fechaLlegada")LocalDateTime fechaLlegada,
                         @Param("precio")Double precio,
                         @Param("asientos")int asientos,
                         @Param("idTipoVuelo")Long idTipoVuelo,
                         @Param("idAerolinea")Long idAerolinea);

    @Transactional
    @Modifying
    @Query(value = "{call insert_vuelo(:origenId, :destinoId, :fechaPartida, :fechaLlegada, :precio, :asientos, :idTipoVuelo, :idAerolinea)}", nativeQuery = true)
    void crearVuelo(@Param("origenId")Long origenId,
                    @Param("destinoId")Long destinoId,
                    @Param("fechaPartida") LocalDateTime fechaPartida,
                    @Param("fechaLlegada")LocalDateTime fechaLlegada,
                    @Param("precio")Double precio,
                    @Param("asientos")int asientos,
                    @Param("idTipoVuelo")Long idTipoVuelo,
                    @Param("idAerolinea")Long idAerolinea);

    // ********** vuelos con fecha *************
//    @Query(value = "select * from vuelos where origen = :origen and destino = :destino and date(fechaPartida) = :fechaPartida", nativeQuery = true)
//    List<VueloModel> buscarVuelosDirectosConFecha(@Param("origen")String origen,
//                                                  @Param("destino")String destino,
//                                                  @Param("fechaPartida") LocalDate fechaPartida);
//
//    @Query(value = "select * from vuelos where origen = :origen and date(fechaPartida) = :fechaPartida", nativeQuery = true)
//    List<VueloModel> buscarVuelosConSoloOrigenConFecha(@Param("origen")String origen, @Param("fechaPartida") LocalDate fechaPartida);

// ********** vuelos sin fecha *************
//    @Query(value = "select * from vuelos where origen = :origen and destino = :destino", nativeQuery = true)
//    List<VueloModel> buscarVuelosDirectosSinFecha(@Param("origen")String origen, @Param("destino")String destino);
//
//    @Query(value = "select * from vuelos where origen = :origen", nativeQuery = true)
//    List<VueloModel> buscarVuelosConSoloOrigenSinFecha(@Param("origen")String origen);

    // revisar si puede ser de la misma fecha o otra fecha
//    @Query(value = "select * from vuelos where origen = :destinoRes and destino = :destino and fechaPartida > :fechaLlegada", nativeQuery = true)
//    List<VueloModel> buscarSegundoVueloUnaEscala(@Param("destinoRes")Long destinoRes,
//                                                 @Param("destino")String destino,
//                                                 @Param("fechaLlegada") LocalDateTime fechaLlegada);
//
//    @Query(value = "select * from vuelos where  destino = :destino and fechaPartida > :fechaPartida and date(fechaPartida) = date(:fechaPartida)", nativeQuery = true)
//    List<VueloModel> buscarTercerVueloDosEscalas(@Param("destino")String destino, @Param("fechaPartida") LocalDateTime fechaPartida);
//
//    @Query(value = "select * from vuelos where origen = :destinoRes and destino = :origenRes and fechaPartida > :fechaLlegadaRes1 and fechaLlegada < :fechaPartidaRes2", nativeQuery = true)
//    List<VueloModel> buscarVuelosIntermidiosDosEscalas(@Param("destinoRes")Long destinoRes,
//                                                       @Param("origenRes")Long origenRes,
//                                                       @Param("fechaLlegadaRes1") LocalDateTime fechaLlegadaRes1,
//                                                       @Param("fechaPartidaRes2") LocalDateTime fechaPartidaRes2);

// ******** obtener vuelo por codigo
    Optional<VueloModel> findByCodigoVuelo(String codigoVuelo);
}
