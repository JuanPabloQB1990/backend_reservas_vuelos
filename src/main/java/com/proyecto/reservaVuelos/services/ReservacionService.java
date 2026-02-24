package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.*;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.ClienteMapper;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.models.EstadoReservacion;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.models.ReservacionModel;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;
import com.proyecto.reservaVuelos.repositories.ReservacionRepository;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservacionService {

    private ReservacionRepository reservacionRepository;
    private VueloRepository vueloRepository;
    private ClienteRepository clienteRepository;

    @Autowired
    public ReservacionService(
            ReservacionRepository reservacionRepository,
            VueloRepository vueloRepository,
            ClienteRepository clienteRepository,
            VueloService vueloService,
            VueloMapper vueloMapper,
            ClienteMapper clienteMapper
    ) {
        this.reservacionRepository = reservacionRepository;
        this.vueloRepository = vueloRepository;
        this.clienteRepository = clienteRepository;


    }

    private String generarCodigoReservacion() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public ResponseEntity<FacturaReservacionDto> crearReservacion(CrearReservaDto dto) throws EntityNotFoundException {

        // Validaciones básicas
        if (dto.getNumeroAsientos() == null || dto.getNumeroAsientos() < 1) {
            return ResponseEntity.badRequest().body(
                    FacturaReservacionDto.builder()
                            .message("Debe reservar al menos 1 persona")
                            .errors(List.of("numeroAsientos"))
                            .build()
            );
        }

        ClienteModel cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new EntityNotFoundException("Persona no registrada"));

        // Eliminar vuelos repetidos
        List<Long> vuelosUnicos = dto.getVuelosIds().stream().distinct().collect(Collectors.toList());

        List<VueloModel> listaVuelos = new ArrayList<>();
        double total = 0;

        for (Long idVuelo : vuelosUnicos) {
            VueloModel vuelo = vueloRepository.findById(idVuelo)
                    .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado: " + idVuelo));

            if (vuelo.getFechaPartida().minusHours(3).isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("La reserva debe hacerse con mínimo 3 horas de anticipación para el vuelo " + idVuelo);
            }

            if (vuelo.getAsientos() < dto.getNumeroAsientos()) {
                throw new IllegalStateException("No hay suficientes asientos para el vuelo " + idVuelo);
            }

            vuelo.setAsientos(vuelo.getAsientos() - dto.getNumeroAsientos());
            vueloRepository.save(vuelo);

            listaVuelos.add(vuelo);
            total += vuelo.getPrecio() * dto.getNumeroAsientos();
        }

        // Crear reservación escalable
        ReservacionModel reservacion = new ReservacionModel();
        reservacion.setCliente(cliente);
        reservacion.setNumeroAsientos(dto.getNumeroAsientos());
        reservacion.setTotal(total);
        reservacion.setFechaReservacion(LocalDateTime.now());
        reservacion.setCodigoReservacion(generarCodigoReservacion());
        reservacion.setVuelos(listaVuelos);

        reservacionRepository.save(reservacion);

        List<VueloFacturaDto> vuelosFactura = listaVuelos.stream()
                .map(v -> new VueloFacturaDto(
                        v.getCodigoVuelo(),
                        v.getOrigen().getNombreAeropuerto(),
                        v.getDestino().getNombreAeropuerto(),
                        v.getFechaPartida(),
                        v.getPrecio()
                ))
                .toList();

        // Devolver factura
        FacturaReservacionDto factura = FacturaReservacionDto.builder()
                .message("Reserva realizada correctamente")
                .codigo(reservacion.getCodigoReservacion())
                .fechaReserva(reservacion.getFechaReservacion())
                .vuelos(vuelosFactura)
                .asientos(dto.getNumeroAsientos())
                .total_a_pagar(total)
                .errors(null)
                .build();

        return new ResponseEntity<>(factura, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Object> cancelarReservacionPorId(Long idReservacion) throws EntityNotFoundException {
        Optional<ReservacionModel> reservacion = reservacionRepository.findById(idReservacion);

        if (reservacion.isEmpty()) {

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Reservación no encontrada");
            response.put("codigoBuscado", idReservacion);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (reservacion.get().getEstado() == EstadoReservacion.CANCELADA) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "La reservación ya está cancelada");
            return ResponseEntity.badRequest().body(response);
        }

        // devolver asientos a los vuelos
        for (VueloModel vuelo : reservacion.get().getVuelos()) {

            vuelo.setAsientos(
                    vuelo.getAsientos() + reservacion.get().getNumeroAsientos()
            );

            vueloRepository.save(vuelo);
        }

        reservacion.get().setEstado(EstadoReservacion.CANCELADA);

        reservacionRepository.save(reservacion.get());

        Map<String, Object> response = new HashMap<>();

        response.put("mensaje", "Reservación cancelada correctamente");
        response.put("codigoReservacion", reservacion.get().getCodigoReservacion());
        response.put("fechaCancelacion", LocalDateTime.now());

        return ResponseEntity.ok(response);

    }

    public ResponseEntity<List<ReservacionModelDto>> obtenerReservacionesPorIdCliente(Long idCliente) throws EntityNotFoundException {

        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Persona no Registrada"));

        List<ReservacionModel> reservaciones = reservacionRepository.obtenerReservacionesCliente(cliente.getIdCliente());

        List<ReservacionModelDto> respuesta = reservaciones.stream().map(reserva -> {

            List<VueloResumenDto> vuelos = reserva.getVuelos().stream()
                    .map(vuelo -> VueloResumenDto.builder()
                            .codigoVuelo(vuelo.getCodigoVuelo())
                            .origen(vuelo.getOrigen().getNombreAeropuerto())
                            .destino(vuelo.getDestino().getNombreAeropuerto())
                            .salida(vuelo.getFechaPartida())
                            .build())
                    .toList();

            return ReservacionModelDto.builder()
                    .idReservacion(reserva.getIdReservacion())
                    .codigoReservacion(reserva.getCodigoReservacion())
                    .fechaReservacion(reserva.getFechaReservacion())
                    .asientos(reserva.getNumeroAsientos())
                    .total(reserva.getTotal())
                    .vuelos(vuelos)
                    .build();

        }).toList();

        return ResponseEntity.ok(respuesta);


    }


}
