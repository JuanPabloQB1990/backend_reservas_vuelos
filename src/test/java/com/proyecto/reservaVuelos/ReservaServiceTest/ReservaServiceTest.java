package com.proyecto.reservaVuelos.ReservaServiceTest;

import com.proyecto.reservaVuelos.dto.CrearReservaDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.ClienteMapper;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.*;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;
import com.proyecto.reservaVuelos.repositories.ReservacionRepository;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import com.proyecto.reservaVuelos.services.ReservacionService;
import com.proyecto.reservaVuelos.services.VueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservaServiceTest {

    private ReservacionRepository reservacionRepository;
    private VueloRepository vueloRepository;
    private ClienteRepository clienteRepository;
    private VueloService vueloService;
    private ReservacionService reservacionService;
    private VueloMapper vueloMapper;
    private ClienteMapper clienteMapper;

    LocalDateTime fechaSalida;
    LocalDateTime fechaLlegada;
    TipoVueloModel tipoVuelo;
    AerolineaModel aerolinea;
    ClienteModel cliente;
    VueloModel vuelo1;
    VueloModel vuelo2;
    VueloModel vuelo3;
    CrearReservaDto reservacion;
    ReservacionModel reservacionHecha;

    @BeforeEach
    public void config(){
        this.clienteRepository = mock(ClienteRepository.class);
        this.reservacionRepository = mock(ReservacionRepository.class);
        this.vueloRepository = mock(VueloRepository.class);
        this.vueloService = mock(VueloService.class);
        this.vueloMapper = mock(VueloMapper.class);
        this.clienteMapper = mock(ClienteMapper.class);

        this.reservacionService = new ReservacionService(this.reservacionRepository,this.vueloRepository,this.clienteRepository,this.vueloService,this.vueloMapper,this.clienteMapper);
        fechaSalida = LocalDateTime.now().plusHours(4);
        fechaLlegada = LocalDateTime.now().plusHours(5);
        tipoVuelo = new TipoVueloModel(1L, "PUBLICO");
        aerolinea = new AerolineaModel(1L, "AVIANCA");
        cliente = new ClienteModel(1L, "Juan","Quintero","321356265", "juan@gmail.com","juan123", "1234", "Colombia", "Medellin", "cll 55 d634",RolModel.EMPLEADO );

        vuelo1 = new VueloModel(1L, "AV0001","Bogota","Medellin",fechaSalida, fechaLlegada,2000, 100, tipoVuelo, aerolinea);
        vuelo2 = new VueloModel(2L, "AV0002","Medellin","Cali",LocalDateTime.of(2023,11,20,21,30), LocalDateTime.of(2023,11,20,23,30),2000, 100, tipoVuelo, aerolinea);
        vuelo3 = new VueloModel(3L, "AV0003","Cali","Leticia",fechaSalida, fechaLlegada,2000, 100, tipoVuelo, aerolinea);

        reservacion = new CrearReservaDto(vuelo1.getIdVuelo(), 2, cliente.getIdCliente());

        reservacionHecha = new ReservacionModel(
                "1234-asdf",
                vuelo1,
                LocalDateTime.now(),
                reservacion.getAsientos(),
                cliente
        );
    }

    @Test
    @DisplayName("reserva con un solo vuelo exitosa")
    public void testReservaUnVuelo() throws EntityNotFoundException {

        when(this.vueloRepository.findById(any())).thenReturn(Optional.of(vuelo1));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));
        when(this.reservacionRepository.save(any())).thenReturn(reservacionHecha);

        ResponseEntity<Object> reservaRealizada = this.reservacionService.crearReservacion(reservacion);

        assertThat(reservaRealizada.getBody()).isNotNull();
        verify(this.vueloRepository, times(2)).findById(any());
        verify(this.clienteRepository, times(1)).findById(any());
        verify(this.vueloService, times(1)).actualizarVuelo(any(),any());
        verify(this.reservacionRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("reserva con dos vuelos exitosa")
    public void testReservaDosVuelo() throws EntityNotFoundException {

        CrearReservaDto reservacion2 = new CrearReservaDto(vuelo1.getIdVuelo(), vuelo2.getIdVuelo(), 2, cliente.getIdCliente());

        ReservacionModel reservacionHecha = new ReservacionModel(
                "1234-asdf",
                vuelo1,
                vuelo2,
                LocalDateTime.now(),
                reservacion.getAsientos(),
                cliente
        );

        when(this.vueloRepository.findById(1L)).thenReturn(Optional.of(vuelo1));
        when(this.vueloRepository.findById(2L)).thenReturn(Optional.of(vuelo2));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));
        when(this.reservacionRepository.save(any())).thenReturn(reservacionHecha);

        ResponseEntity<Object> reservaRealizada = this.reservacionService.crearReservacion(reservacion2);

        assertThat(reservaRealizada.getBody()).isNotNull();
        verify(this.vueloRepository, times(3)).findById(any());
        verify(this.clienteRepository, times(1)).findById(any());
        verify(this.vueloService, times(2)).actualizarVuelo(any(),any());
        verify(this.reservacionRepository, times(1)).save(any());


    }

    @Test
    @DisplayName("reserva con tres vuelos exitosa")
    public void testReservaTresVuelo() throws EntityNotFoundException {

        CrearReservaDto reservacion = new CrearReservaDto(vuelo1.getIdVuelo(), vuelo2.getIdVuelo(), vuelo3.getIdVuelo(),2, cliente.getIdCliente());

        ReservacionModel reservacionHecha = new ReservacionModel(
                "1234-asdf",
                vuelo1,
                vuelo2,
                vuelo3,
                LocalDateTime.now(),
                reservacion.getAsientos(),
                cliente
        );

        when(this.vueloRepository.findById(1L)).thenReturn(Optional.of(vuelo1));
        when(this.vueloRepository.findById(2L)).thenReturn(Optional.of(vuelo2));
        when(this.vueloRepository.findById(3L)).thenReturn(Optional.of(vuelo3));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));
        when(this.reservacionRepository.save(any())).thenReturn(reservacionHecha);

        ResponseEntity<Object> reservaRealizada = this.reservacionService.crearReservacion(reservacion);

        assertThat(reservaRealizada.getBody()).isNotNull();
        verify(this.vueloRepository, times(4)).findById(any());
        verify(this.clienteRepository, times(1)).findById(any());
        verify(this.vueloService, times(3)).actualizarVuelo(any(),any());
        verify(this.reservacionRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("excepcion intento reserva a menos de 3 horas de salida")
    public void testExcepcionIntentoReservaMenosTresHoras() throws EntityNotFoundException {
        LocalDateTime fechaSalida = LocalDateTime.now().plusHours(2);
        LocalDateTime fechaLlegada = LocalDateTime.now().plusHours(3);
        TipoVueloModel tipoVuelo = new TipoVueloModel(1L, "PUBLICO");
        AerolineaModel aerolinea = new AerolineaModel(1L, "AVIANCA");
        ClienteModel cliente = new ClienteModel(1L, "Juan","Quintero","321356265", "juan@gmail.com","juan123", "1234", "Colombia", "Medellin", "cll 55 d634", RolModel.EMPLEADO );

        VueloModel vuelo = new VueloModel(1L, "AV0001","Bogota","Medellin",fechaSalida, fechaLlegada,2000, 100, tipoVuelo, aerolinea);
        CrearReservaDto reservacion = new CrearReservaDto(vuelo.getIdVuelo(), 2, cliente.getIdCliente());

        when(this.vueloRepository.findById(any())).thenReturn(Optional.of(vuelo));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));

        assertThrows(EntityNotFoundException.class, ()-> this.reservacionService.crearReservacion(reservacion));

        verify(this.vueloRepository, times(1)).findById(any());
        verify(this.clienteRepository, never()).findById(any());
        verify(this.vueloService, never()).actualizarVuelo(any(),any());
        verify(this.reservacionRepository, never()).save(any());

    }

    @Test
    @DisplayName("excepcion cuando cliente no esta registrado")
    public void testExcepcionClienteNoRegistrado() throws EntityNotFoundException {

        when(this.vueloRepository.findById(any())).thenReturn(Optional.of(vuelo1));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, ()-> this.reservacionService.crearReservacion(reservacion));

        verify(this.vueloRepository, times(1)).findById(any());
        verify(this.clienteRepository, times(1)).findById(any());
        verify(this.vueloService, never()).actualizarVuelo(any(),any());
        verify(this.reservacionRepository,never()).save(any());

    }

    @Test
    @DisplayName("excepcion numero de asientos a reservar no disponibles")
    public void testExcepcionNumeroAcientosNoDisponibles() throws EntityNotFoundException {
        VueloModel vuelo = new VueloModel(1L, "AV0001","Bogota","Medellin",fechaSalida, fechaLlegada,2000, 5, tipoVuelo, aerolinea);

        CrearReservaDto reservacionAsientosNoDisponibles = new CrearReservaDto(vuelo.getIdVuelo(), 6, cliente.getIdCliente());

        List<Number> listaIdsVuelos = new ArrayList<>();
        listaIdsVuelos.add(vuelo1.getIdVuelo());

        when(this.vueloRepository.findById(any())).thenReturn(Optional.of(vuelo));
        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));

        assertThrows(EntityNotFoundException.class, ()-> this.reservacionService.crearReservacion(reservacionAsientosNoDisponibles));

        verify(this.vueloRepository, times(listaIdsVuelos.size()+1)).findById(any());
        verify(this.clienteRepository, times(1)).findById(any());
        verify(this.vueloService, never()).actualizarVuelo(any(),any());
        verify(this.reservacionRepository, never()).save(any());

    }

    @Test
    @DisplayName("cancelar reserva exitosamente")
    public void testCancelarReservaExitosamente() throws EntityNotFoundException {

        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));
        when(this.reservacionRepository.findByCodigoReservacion(any())).thenReturn(reservacionHecha);

        ResponseEntity<Object> reservaResponse = this.reservacionService.cancelarReservacionPorId("1234-asdf");

        verify(this.reservacionRepository, times(1)).deleteById(any());
        assertThat(reservaResponse).isNotNull();

    }

    @Test
    @DisplayName("excpecion para cancelar reserva no existente")
    public void testExcepcionCancelarReservaNoExiste() {

        when(this.clienteRepository.findById(any())).thenReturn(Optional.of(cliente));
        when(this.reservacionRepository.findByCodigoReservacion(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> this.reservacionService.cancelarReservacionPorId("1234-asdf"));

        verify(this.reservacionRepository, never()).deleteById(any());

    }

    @Test
    @DisplayName("excpecion para cancelar reserva cliente no existente")
    public void testExcepcionCancelarReservaClienteNoExiste() {

        when(this.clienteRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(this.reservacionRepository.findByCodigoReservacion(any())).thenReturn(reservacionHecha);

        assertThrows(EntityNotFoundException.class, () -> this.reservacionService.cancelarReservacionPorId("1234-asdf"));

        verify(this.reservacionRepository, never()).deleteById(any());

    }



}
