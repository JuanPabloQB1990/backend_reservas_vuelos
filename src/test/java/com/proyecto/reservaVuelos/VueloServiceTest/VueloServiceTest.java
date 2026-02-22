package com.proyecto.reservaVuelos.VueloServiceTest;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.TipoVueloModel;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.repositories.AeropuertoRepository;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import com.proyecto.reservaVuelos.services.VueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VueloServiceTest {

    @Mock
    private VueloRepository vueloRepository;
    private AeropuertoRepository aeropuertoRepository;

    @InjectMocks
    private VueloService vueloService;
    private VueloMapper vueloMapper;

    private TipoVueloModel tipoVuelo;
    private AerolineaModel aerolinea;

    private VueloModel vuelo1;
    private VueloModel vuelo2;
    private VueloModel vuelo3;

    private VueloModel vuelo4;
    private VueloModel vuelo5;
    private VueloModel vuelo6;
    private VueloModel vuelo7;
    private VueloModelDto vuelo8;

    private AeropuertoModel aeropuertoModel1;
    private AeropuertoModel aeropuertoModel2;
    private AeropuertoModel aeropuertoModel3;
    private AeropuertoModel aeropuertoModel4;
    private AeropuertoModel aeropuertoModel5;
    private AeropuertoModel aeropuertoModel6;
    private AeropuertoModel aeropuertoModel7;
    private AeropuertoModel aeropuertoModel8;
    private AeropuertoModel aeropuertoModel9;
    private AeropuertoModel aeropuertoModel10;
    private AeropuertoModel aeropuertoModel11;
    private AeropuertoModel aeropuertoModel12;
    private AeropuertoModel aeropuertoModel13;
    private AeropuertoModel aeropuertoModel14;
    private AeropuertoModel aeropuertoModel15;
    private AeropuertoModel aeropuertoModel16;

    @BeforeEach
    public void config(){
        this.vueloMapper = mock(VueloMapper.class);
        MockitoAnnotations.openMocks(this);
        this.vueloService = new VueloService(this.vueloRepository, this.vueloMapper, this.aeropuertoRepository);
        tipoVuelo = new TipoVueloModel(1L, "Publico");
        aerolinea = new AerolineaModel(1L, "Avianca");
        aeropuertoModel1 = new AeropuertoModel("MDE","Medellin","Aeropuerto Internacional Jose Maria Cordova","Colombia");
        aeropuertoModel2 = new AeropuertoModel("CTG","Cartagena","Aeropuerto Internacional Rafael Nunez","Colombia");
        aeropuertoModel3 = new AeropuertoModel("CLO","Cali","Aeropuerto Internacional Alfonso Bonilla Aragon","Colombia");
        aeropuertoModel4 = new AeropuertoModel("BAQ","Barranquilla","Aeropuerto Internacional Ernesto Cortissoz","Colombia");
        aeropuertoModel5 = new AeropuertoModel("JFK","New York","John F. Kennedy International Airport","USA");
        aeropuertoModel6 = new AeropuertoModel("LAX","Los Angeles","Los Angeles International Airport","USA");
        aeropuertoModel7 = new AeropuertoModel("ATL","Atlanta","Hartsfield Jackson Atlanta International Airport","USA");
        aeropuertoModel8 = new AeropuertoModel("MIA","Miami","Miami International Airport","USA");
        aeropuertoModel9 =  new AeropuertoModel("ORD","Chicago","O Hare International Airport","USA");
        aeropuertoModel10 = new AeropuertoModel("DFW","Dallas","Dallas Fort Worth International Airport","USA");
        aeropuertoModel11 = new AeropuertoModel("MEX","Ciudad de Mexico","Aeropuerto Internacional Benito Juarez","Mexico");
        aeropuertoModel12 = new AeropuertoModel("DEN","Denver","Denver International Airport","USA");
        aeropuertoModel13 = new AeropuertoModel("SEA","Seattle","Seattle Tacoma International Airport","USA");
        aeropuertoModel14 = new AeropuertoModel("LAS","Las Vegas","Harry Reid International Airport","USA");
        aeropuertoModel15 = new AeropuertoModel("SFO","San Francisco","San Francisco International Airport","USA");
        aeropuertoModel16 = new AeropuertoModel("CUN","Cancun","Aeropuerto Internacional de Cancun","Mexico");

        vuelo1 = new VueloModel(
                1L,
                "AV001",
                aeropuertoModel1,
                aeropuertoModel2,
                LocalDateTime.of(2023,11,9,9,00),
                LocalDateTime.of(2023,11,9,10,00),
                2000.5,
                180,
                tipoVuelo,
                aerolinea
                );

        vuelo2 = new VueloModel(
                1L,
                "AV002",
                aeropuertoModel3,
                aeropuertoModel4,
                LocalDateTime.of(2023,11,9,12,00),
                LocalDateTime.of(2023,11,9,13,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo3 = new VueloModel(
                3L,
                "AV002",
                aeropuertoModel5,
                aeropuertoModel6,
                LocalDateTime.of(2023,11,9,15,00),
                LocalDateTime.of(2023,11,9,16,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo4 = new VueloModel(
                4L,
                "AV002",
                aeropuertoModel7,
                aeropuertoModel8,
                LocalDateTime.of(2023,11,9,12,00),
                LocalDateTime.of(2023,11,9,13,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo5 = new VueloModel(
                5L,
                "AV002",
                aeropuertoModel9,
                aeropuertoModel10,
                LocalDateTime.of(2023,11,9,11,00),
                LocalDateTime.of(2023,11,9,12,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo6 = new VueloModel(
                6L,
                "AV002",
                aeropuertoModel10,
                aeropuertoModel12,
                LocalDateTime.of(2023,11,9,18,00),
                LocalDateTime.of(2023,11,9,17,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo7 = new VueloModel(
                7L,
                "AV002",
                aeropuertoModel13,
                aeropuertoModel14,
                LocalDateTime.of(2023,11,9,16,30),
                LocalDateTime.of(2023,11,9,17,00),
                2000,
                100,
                tipoVuelo,
                aerolinea
        );

        vuelo8 = VueloModelDto.build(
                1L,
                "AV001",
                aeropuertoModel15,
                aeropuertoModel16,
                LocalDateTime.of(2024,11,9,16,30),
                LocalDateTime.of(2024,11,9,17,00),
                5000,
                200,
                "Publico",
                "Avianca"
                );

    }

    @Test
    @DisplayName("Busqueda de vuelo por id de vuelo exitosamente")
    public void testObtenerVueloExitoso() throws EntityNotFoundException {
        when(this.vueloRepository.findById(any())).thenReturn(Optional.ofNullable(vuelo1));
        when(this.vueloMapper.toVueloDto(any())).thenReturn(vuelo8);
        VueloModelDto vueloEncontrado = this.vueloService.obtenerVueloPorId(1L);

        assertNotNull(vueloEncontrado);
        assertThat(vueloEncontrado.getTipoVuelo()).hasToString("Publico");
    }


    @Test
    @DisplayName("excepcion no encuentra vuelo por id")
    public void testExcepcionBuscandoVueloConFecha() throws EntityNotFoundException {
        when(this.vueloRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, () -> this.vueloService.obtenerVueloPorId(any()));

    }

    // ****************** con fecha ************
//    @Test
//    @DisplayName("busqueda de vuelos directos con fecha con exito")
//    public void testBusquedaVuelosDirectosConFechaExitosa() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosDirectosConFecha(any(),any(), any())).thenReturn(List.of(vuelo1));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.buscarVuelosConEscalas("Bogota","Medellin",LocalDateTime.of(2024,11,9,16,30));
//
//        assertThat(vuelos.size()).isEqualTo(1);
//
//    }

//    @Test
//    @DisplayName("excepcion no encuentra vuelos directos con fecha")
//    public void testBusquedaVuelosDirectosConFechaEsxcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosDirectosConFecha(any(),any(), any())).thenReturn(List.of());
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.buscarVuelosConEscalas("Bogota","Medellin",LocalDate.of(2023,11,9)));
//
//    }

//    @Test
//    @DisplayName("busqueda de vuelos con una escala con fecha con exito")
//    public void testBusquedaUnaEscalasConFechaExitosa() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(),any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of(vuelo4));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.buscarVuelosConEscalas(any(),any(),any());
//
//        assertThat(vuelos).hasSize(1);
//
//    }
//
//    @Test
//    @DisplayName("excepcion no encuentra una escala con fecha ")
//    public void testBusquedaUnaEscalasConFechaExcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(),any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of());
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.buscarVuelosConEscalas(any(),any(),any()));
//
//    }
//
//    @Test
//    @DisplayName("busqueda de vuelos con dos escalas con fecha con exito")
//    public void testBusquedaDosEscalasConFechaExitosa() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(),any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo3));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo2));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.obtenerTodosLosVuelosConFecha(any(),any(),any());
//
//        assertThat(vuelos).hasSize(1);
//
//    }
//
//    @Test
//    @DisplayName("excepcion no encuentra vuelos con dos escalas con fecha ")
//    public void testBusquedaDosEscalasConFechaExcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(),any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo2));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo4));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosConFecha(any(),any(),any()));
//
//    }
//
//    @Test
//    @DisplayName("validar la hora entre vuelos con dos escalas con fecha")
//    public void testValidarUnaHoraEntreVuelosDosEscalasConFecha() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(), any())).thenReturn(List.of(vuelo2));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo7));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo3));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosConFecha(any(),any(), any()));
//
//    }
//
//    @Test
//    @DisplayName("validar mas de una hora entre vuelos con una escala con fecha")
//    public void testValidarUnaHoraEntreVuelosUnaEscalaConFecha() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenConFecha(any(),any())).thenReturn(List.of(vuelo3));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of(vuelo7));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosConFecha(any(),any(),any()));
//
//    }
//
//
//    //******************** test busqueda sin fecha ****************
//
//    @Test
//    @DisplayName("test para encontrar vuelos directos sin fecha con exito")
//    public void testBusquedaVuelosDirectosSinFechaExitosa() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosDirectosSinFecha(any(),any())).thenReturn(List.of(vuelo1));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.obtenerTodosLosVuelosSinFecha("Bogota","Medellin");
//
//        assertThat(vuelos).isNotEmpty();
//        assertThat(vuelos.size()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("test para encontrar vuelos directos sin fecha excepcion")
//    public void testBusquedaVuelosDirectosSinFechaExcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosDirectosSinFecha(any(),any())).thenReturn(List.of());
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosSinFecha("Bogota","Medellin"));
//
//    }
//
//    //********* una escala sin fecha **********
//    @Test
//    @DisplayName("validar mas de una hora entre vuelos con una escala sin fecha")
//    public void testValidarUnaHoraEntreVuelosUnaEscala() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo3));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of(vuelo7));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any()));
//
//    }
//
//    @Test
//    @DisplayName("busqueda de vuelo con una escala exitosa sin fecha")
//    public void testBusquedaDeVueloConUnaEscala() throws EntityNotFoundException {
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of(vuelo4));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any());
//
//        assertThat(vuelos).isNotEmpty();
//        assertThat(vuelos.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("test excepcion encontrar una escala sin fecha")
//    public void testBusquedaUnaEscalasSinFechaExcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarSegundoVueloUnaEscala(any(),any(),any())).thenReturn(List.of());
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any()));
//
//    }
//
//    //************ sin fecha dos escalas ************
//
//    @Test
//    @DisplayName("busqueda de vuelos con dos escala sin fecha con exito")
//    public void testBusquedaDosEscalasSinFechaExitosa() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo2));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo6));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo3));
//
//        Stack<List<VueloModelDto>> vuelos = this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any());
//
//        assertThat(vuelos).isNotEmpty();
//        assertThat(vuelos.size()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("test para validar la hora entre vuelos con dos escalas sin fecha")
//    public void testValidarUnaHoraEntreVuelosDosEscalas() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo2));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo7));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo3));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any()));
//
//    }
//
//    @Test
//    @DisplayName("test excepcion encontrar dos escala sin fecha ")
//    public void testBusquedaDosEscalasSinFechaExcepcion() throws EntityNotFoundException {
//
//        when(this.vueloRepository.buscarVuelosConSoloOrigenSinFecha(any())).thenReturn(List.of(vuelo1));
//        when(this.vueloRepository.buscarTercerVueloDosEscalas(any(),any())).thenReturn(List.of(vuelo2));
//        when(this.vueloRepository.buscarVuelosIntermidiosDosEscalas(any(),any(), any(), any())).thenReturn(List.of(vuelo4));
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.obtenerTodosLosVuelosSinFecha(any(),any()));
//
//    }
//
//    // ************* test crear vuelos
//
//    @Test
//    @DisplayName("validar creacion de vuelos")
//    public void testCrearVuelos() throws EntityNotFoundException {
//
//        ResponseEntity<Object> vueloCreado = this.vueloService.crearVuelo();
//
//        assertNotNull(vuelo1);
//        assertThat(vueloCreado.getBody()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("validar actualizacion vuelo null")
//    public void testActualizarVueloNull() throws EntityNotFoundException {
//    when(vueloRepository.findById(any())).thenReturn(Optional.ofNullable(null));
//    verify(vueloRepository, never()).actualizarVuelo(
//            vuelo1.getIdVuelo(),
//            vuelo1.getOrigenId(),
//            vuelo1.getDestino(),
//            vuelo1.getFechaPartida(),
//            vuelo1.getFechaLlegada(),
//            vuelo1.getPrecio(),
//            vuelo1.getAsientos(),
//            vuelo1.getTipoVuelo().getIdTipoVuelo(),
//            vuelo1.getAerolinea().getIdAerolinea());
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.actualizarVuelo(any(),vuelo1));
//    }
//
//    @Test
//    @DisplayName("validar actualizacion vuelo con mismo id exitoso")
//    public void testActualizarVueloExitoso() throws EntityNotFoundException {
//        when(vueloRepository.findById(any())).thenReturn(Optional.ofNullable(vuelo1));
//
//        ResponseEntity<Object> response = this.vueloService.actualizarVuelo(1L, vuelo2);
//
//        verify(vueloRepository, never()).actualizarVuelo(
//                vuelo2.getIdVuelo(),
//                vuelo2.getOrigenId(),
//                vuelo2.getDestinoId(),
//                vuelo2.getFechaPartida(),
//                vuelo2.getFechaLlegada(),
//                vuelo2.getPrecio(),
//                vuelo2.getAsientos(),
//                vuelo2.getTipoVuelo().getIdTipoVuelo(),
//                vuelo2.getAerolinea().getIdAerolinea());
//
//
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("validar eliminacion vuelo exitoso")
//    public void testEliminarVueloExitoso() throws EntityNotFoundException {
//
//        when(vueloRepository.findById(any())).thenReturn(Optional.ofNullable(vuelo1));
//
//        ResponseEntity<Object> response = this.vueloService.eliminarVueloPorId(1L);
//
//        verify(vueloRepository, times(1)).deleteById(1L);
//
//        assertThat(response.getStatusCodeValue()).isEqualTo(204);
//    }
//
//    @Test
//    @DisplayName("validar eliminacion vuelo excepcion")
//    public void testEliminarVueloExcepcion() throws EntityNotFoundException {
//
//        when(vueloRepository.findById(any())).thenReturn(Optional.ofNullable(null));
//
//        verify(vueloRepository, never()).deleteById(1L);
//
//        assertThrows(EntityNotFoundException.class, ()-> this.vueloService.eliminarVueloPorId(any())).getMessage();
//    }

}
