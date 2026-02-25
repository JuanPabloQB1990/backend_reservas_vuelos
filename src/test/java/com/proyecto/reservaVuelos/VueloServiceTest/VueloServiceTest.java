package com.proyecto.reservaVuelos.VueloServiceTest;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import com.proyecto.reservaVuelos.repositories.AeropuertoRepository;
import com.proyecto.reservaVuelos.repositories.TipoVueloRepository;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import com.proyecto.reservaVuelos.services.VueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VueloServiceTest {

    @Mock
    private VueloRepository vueloRepository;
    private AeropuertoRepository aeropuertoRepository;
    private AerolineaRepository aerolineaRepository;
    private TipoVueloRepository tipoVueloRepository;

    @InjectMocks
    private VueloService vueloService;
    private VueloMapper vueloMapper;

    @BeforeEach
    public void config(){
        this.vueloMapper = mock(VueloMapper.class);
        MockitoAnnotations.openMocks(this);
        this.vueloService = new VueloService(this.vueloRepository, this.vueloMapper, this.aeropuertoRepository, this.aerolineaRepository, this.tipoVueloRepository);
    }

    @Test
    void deberiaRetornarVueloCuandoExiste() throws EntityNotFoundException {
        // Arrange
        Long id = 1L;

        VueloModel vueloModel = new VueloModel();
        VueloModelDto vueloDto = new VueloModelDto();

        when(vueloRepository.findById(id)).thenReturn(Optional.of(vueloModel));
        when(vueloMapper.toVueloDto(vueloModel)).thenReturn(vueloDto);

        // Act
        VueloModelDto resultado = vueloService.obtenerVueloPorId(id);

        // Assert
        verify(vueloRepository, times(1)).findById(id);
        assertNotNull(resultado);
        assertEquals(vueloDto, resultado);

        verify(vueloRepository).findById(id);
        verify(vueloMapper).toVueloDto(vueloModel);
    }

    @Test
    void deberiaLanzarExcepcionCuandoVueloNoExiste() {
        // Arrange
        Long id = 1L;

        when(vueloRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> vueloService.obtenerVueloPorId(id)
        );

        assertEquals("Vuelo no encontrado", exception.getMessage());

        verify(vueloRepository, times(1)).findById(id);
        verify(vueloRepository).findById(id);
        verifyNoInteractions(vueloMapper);
    }

    @Test
    void deberiaRetornarPaginaDeVuelosCuandoExisten() throws EntityNotFoundException {

        PageRequest pageable = PageRequest.of(0, 5);

        VueloModel vuelo1 = new VueloModel();
        VueloModel vuelo2 = new VueloModel();

        VueloModelDto dto1 = new VueloModelDto();
        VueloModelDto dto2 = new VueloModelDto();

        List<VueloModel> lista = List.of(vuelo1, vuelo2);
        Page<VueloModel> page = new PageImpl<>(lista, pageable, lista.size());

        when(vueloRepository.findAll(pageable)).thenReturn(page);
        when(vueloMapper.toVueloDto(any())).thenReturn(new VueloModelDto());

        Page<VueloModelDto> resultado = vueloService.obtenerTodosLosVuelos(pageable);

        assertEquals(2, resultado.getContent().size());

        verify(vueloRepository).findAll(pageable);
        verify(vueloMapper, times(2)).toVueloDto(any(VueloModel.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoHayVuelos() {

        // Arrange
        PageRequest pageable = PageRequest.of(0, 5);
        Page<VueloModel> pageVacia = Page.empty();

        when(vueloRepository.findAll(pageable)).thenReturn(pageVacia);

        // Act + Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> vueloService.obtenerTodosLosVuelos(pageable)
        );

        assertEquals("no hay vuelos programados", exception.getMessage());

        verify(vueloRepository).findAll(pageable);
        verifyNoInteractions(vueloMapper);
    }

    @Test
    void deberiaEncontrarRutaConEscala() throws EntityNotFoundException {

        String origen = "Bogota";
        String destino = "Madrid";
        int pasajeros = 1;

        LocalDateTime fecha = LocalDateTime.now();

        // CIUDADES
        AeropuertoModel origenCiudad = new AeropuertoModel();
        origenCiudad.setCiudad("Bogota");
        origenCiudad.setPais("Colombia");

        AeropuertoModel escalaCiudad = new AeropuertoModel();
        escalaCiudad.setCiudad("Miami");
        escalaCiudad.setPais("USA");

        AeropuertoModel destinoCiudad = new AeropuertoModel();
        destinoCiudad.setCiudad("Madrid");
        destinoCiudad.setPais("España");

        // VUELO 1
        VueloModel vuelo1 = new VueloModel();
        vuelo1.setOrigen(origenCiudad);
        vuelo1.setDestino(escalaCiudad);
        vuelo1.setFechaLlegada(fecha.plusHours(5));

        // VUELO 2
        VueloModel vuelo2 = new VueloModel();
        vuelo2.setOrigen(escalaCiudad);
        vuelo2.setDestino(destinoCiudad);
        vuelo2.setFechaLlegada(fecha.plusHours(10));

        when(vueloRepository.buscarPrimerTramo(
                any(), any(), any(), anyInt()
        )).thenReturn(List.of(vuelo1));

        when(vueloRepository.buscarConexionesValidas(
                any(), any(), any(), anyInt()
        )).thenReturn(List.of(vuelo2));

        when(vueloMapper.toVueloDto(any())).thenReturn(new VueloModelDto());

        List<List<VueloModelDto>> resultado =
                vueloService.buscarVuelosConEscalas(
                        origen,
                        destino,
                        fecha,
                        pasajeros
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).size());

        verify(vueloRepository).buscarPrimerTramo(any(), any(), any(), anyInt());
        verify(vueloRepository).buscarConexionesValidas(any(), any(), any(), anyInt());
        verify(vueloMapper, atLeastOnce()).toVueloDto(any());
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoHayRutas() {

        String origen = "Bogota";
        String destino = "Madrid";
        int pasajeros = 1;

        LocalDateTime fecha = LocalDateTime.now();

        when(vueloRepository.buscarPrimerTramo(
                any(), any(), any(), anyInt()
        )).thenReturn(List.of());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> vueloService.buscarVuelosConEscalas(
                                origen,
                                destino,
                                fecha,
                                pasajeros
                        )
                );

        assertEquals("no hay vuelos programados", exception.getMessage());

        verify(vueloRepository).buscarPrimerTramo(any(), any(), any(), anyInt());
        verifyNoInteractions(vueloMapper);
    }

    @Test
    void noDebeRepetirCiudadesEnLaRuta() {

        LocalDateTime fecha = LocalDateTime.now();

        AeropuertoModel bogota = new AeropuertoModel("BOG","Bogota","Aeropuerto Internacional El Dorado","Colombia");
        AeropuertoModel miami = new AeropuertoModel("MIA","Miami","Miami International Airport","USA");

        VueloModel vuelo1 = new VueloModel();
        vuelo1.setOrigen(bogota);
        vuelo1.setDestino(miami);
        vuelo1.setFechaLlegada(fecha.plusHours(3));

        // vuelo que vuelve a la misma ciudad
        VueloModel vueloCircular = new VueloModel();
        vueloCircular.setOrigen(miami);
        vueloCircular.setDestino(bogota);
        vueloCircular.setFechaLlegada(fecha.plusHours(6));

        when(vueloRepository.buscarPrimerTramo(any(), any(), any(), anyInt()))
                .thenReturn(List.of(vuelo1));

        when(vueloRepository.buscarConexionesValidas(any(), any(), any(), anyInt()))
                .thenReturn(List.of(vueloCircular));

        assertThrows(
                EntityNotFoundException.class,
                () -> vueloService.buscarVuelosConEscalas(
                        "Bogota",
                        "Madrid",
                        fecha,
                        1
                )
        );
    }

}
