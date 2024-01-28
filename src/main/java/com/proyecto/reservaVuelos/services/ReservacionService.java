package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.CrearReservaDto;
import com.proyecto.reservaVuelos.dto.FacturaReservacionDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservacionService {

    private ReservacionRepository reservacionRepository;
    private VueloRepository vueloRepository;
    private ClienteRepository clienteRepository;
    private VueloService vueloService;

    @Autowired
    public ReservacionService(
            ReservacionRepository reservacionRepository,
            VueloRepository vueloRepository,
            ClienteRepository clienteRepository,
            VueloService vueloService
    ) {
        this.reservacionRepository = reservacionRepository;
        this.vueloRepository = vueloRepository;
        this.clienteRepository = clienteRepository;
        this.vueloService = vueloService;

    }

    HashMap<String, Object> datos;

    List<VueloModel> listaVuelos = new ArrayList<>();

    Optional<ClienteModel> pasajero;

    ReservacionModel reservacion;

    double total = 0;

    public ResponseEntity<Object> crearReservacion(CrearReservaDto reserva) throws EntityNotFoundException {
        listaVuelos.clear();
        total = 0;
        Optional<VueloModel> primerVuelo = vueloRepository.findById(reserva.getIdVuelo1());

        if (primerVuelo.get().getFechaPartida().minusHours(3).isBefore(LocalDateTime.now())){
            throw new EntityNotFoundException("La reserva debe realizarse con al menos 3 horas de anticipación.");

        }else{
            pasajero = this.clienteRepository.findById(reserva.getIdCliente());

            if (pasajero.isPresent()){
                List<Number> listaIdsVuelosReservar = new ArrayList<>();

                if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() == null && reserva.getIdVuelo3() == null){
                    listaIdsVuelosReservar.add(reserva.getIdVuelo1());
                }
                if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() != null && reserva.getIdVuelo3() == null) {
                    listaIdsVuelosReservar.add(reserva.getIdVuelo1());
                    listaIdsVuelosReservar.add(reserva.getIdVuelo2());
                }
                if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() != null && reserva.getIdVuelo3() != null){
                    listaIdsVuelosReservar.add(reserva.getIdVuelo1());
                    listaIdsVuelosReservar.add(reserva.getIdVuelo2());
                    listaIdsVuelosReservar.add(reserva.getIdVuelo3());
                }

                for (Number id : listaIdsVuelosReservar) {

                    // Obtener el vuelo
                    Optional<VueloModel> vuelo = vueloRepository.findById((Long) id);

                    // Asientos disponibles
                    if (vuelo.get().getAsientos() >= reserva.getAsientos()) {

                        // Actualizar el número de asientos disponibles
                        vuelo.get().setAsientos(vuelo.get().getAsientos() - reserva.getAsientos());
                        listaVuelos.add(vuelo.get());
                        vueloService.actualizarVuelo(vuelo.get().getIdVuelo(), vuelo.get());

                    } else {
                        throw new EntityNotFoundException("No hay suficientes asientos disponibles para el vuelo");
                    }

                }
            }else{
                throw new EntityNotFoundException("el cliente no esta registrado");

            }
        }

            // Crear y guardar la reserva en la base de datos
            String codigoReservacion = generarCodigoReservacion();

            if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() == null && reserva.getIdVuelo3() == null){
                reservacion = new ReservacionModel(
                        codigoReservacion,
                        listaVuelos.get(0),
                        LocalDateTime.now(),
                        reserva.getAsientos(),
                        pasajero.get()
                );


            }
            if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() != null && reserva.getIdVuelo3() == null) {
                reservacion = new ReservacionModel(
                        codigoReservacion,
                        listaVuelos.get(0),
                        listaVuelos.get(1),
                        LocalDateTime.now(),
                        reserva.getAsientos(),
                        pasajero.get()
                );

            }
            if (reserva.getIdVuelo1() != null && reserva.getIdVuelo2() != null && reserva.getIdVuelo3() != null){
                reservacion = new ReservacionModel(
                        codigoReservacion,
                        listaVuelos.get(0),
                        listaVuelos.get(1),
                        listaVuelos.get(2),
                        LocalDateTime.now(),
                        reserva.getAsientos(),
                        pasajero.get()
                );

            }

            reservacionRepository.save(reservacion);

            for (int i = 0; i < reserva.getAsientos(); i++) {
                for (VueloModel vuelo: listaVuelos) {
                    total += vuelo.getPrecio();
                }
            }

            return new ResponseEntity<>(
                    FacturaReservacionDto.builder()
                            .message("la reserva se realizo satisfactoriamente")
                            .codigo(reservacion.getCodigoReservacion())
                            .fechaReserva(reservacion.getFechaReservacion())
                            .vuelos(listaVuelos)
                            .asientos(reservacion.getNumeroAsientos())
                            .total_a_pagar(total)
                            .build(),
                    HttpStatusCode.valueOf(201)
            );

    }

    public ResponseEntity<Object> cancelarReservacionPorId(String codigo) throws EntityNotFoundException {
        listaVuelos.clear();
        // Obtener la lista de reservaciones
        ReservacionModel reservacion = this.reservacionRepository.findByCodigoReservacion(codigo);

        if (reservacion != null) {

            Optional<ClienteModel> cliente = this.clienteRepository.findById(reservacion.getCliente().getIdCliente());

            if (cliente.isPresent()){

                if (reservacion.getVuelo1() != null){
                    listaVuelos.add(reservacion.getVuelo1());
                }

                if (reservacion.getVuelo2() != null){

                    listaVuelos.add(reservacion.getVuelo2());

                    if (reservacion.getVuelo3() != null){
                        listaVuelos.add(reservacion.getVuelo3());
                    }
                }

                for (VueloModel vuelo: listaVuelos) {
                    vuelo.setAsientos(vuelo.getAsientos() + reservacion.getNumeroAsientos());
                    this.vueloService.actualizarVuelo(vuelo.getIdVuelo(), vuelo);
                }

                reservacionRepository.deleteById(reservacion.getIdReservacion());

                datos = new HashMap<>();
                datos.put("message", "la reserva se elimino con exito");

                return new ResponseEntity<>(
                        datos,
                        HttpStatus.OK
                );
            }else{
                throw new EntityNotFoundException("el usuario no esta registrado");

            }

        }

        throw new EntityNotFoundException("No se encontraron reservaciones para eliminar");

    }

    private String generarCodigoReservacion() {
        return UUID.randomUUID().toString();
    }

    public List<ReservacionModel> obtenerReservacionesPorIdCliente(Long idCliente) throws EntityNotFoundException {

        Optional<ClienteModel> cliente = this.clienteRepository.findById(idCliente);
        System.out.println(cliente);
        if (cliente.isPresent()){
            List<ReservacionModel> listaReservaciones = this.reservacionRepository.findByCliente(cliente.get());

            if (!listaReservaciones.isEmpty()){
                return listaReservaciones;
            }

            throw new EntityNotFoundException("El cliente no tiene reservaciones");
        }
        throw new EntityNotFoundException("El cliente no esta registrado");

    }

    public ReservacionModel obtenerReservacionPorId(Long idReserva) {
        return this.reservacionRepository.findById(idReserva).get();
    }

}
