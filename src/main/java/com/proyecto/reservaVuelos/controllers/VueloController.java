package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.services.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping(path = "api/vuelos")
@Tag(name = "Vuelos", description = "Catálogo de vuelos")
public class VueloController {

    private VueloService vueloService;

    @Autowired
    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = VueloModelDto.class))),
            @ApiResponse(responseCode = "404", description = "vuelo no encontrado",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "obtener vuelo por id")
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="vuelo/{idVuelo}")
    public VueloModelDto obtenerVueloPorId(@PathVariable("idVuelo") Long idVuelo) throws EntityNotFoundException {
        return this.vueloService.obtenerVueloPorId(idVuelo);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "no hay vuelos programados",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "obtener todos los vuelos")
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path = "all")
    public Page<VueloModelDto> obtenerTodosLosVuelos(Pageable pageable) throws EntityNotFoundException {
        return this.vueloService.obtenerTodosLosVuelos(pageable);
    }

    @Operation(summary = "obtener vuelos por criterio")
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path = "busqueda/criterio")
    public Stack<List<VueloModelDto>> obtenerVuelosPorCriterio(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino,
            @RequestParam("fechaPartida") @Nullable LocalDate fechaPartida) throws EntityNotFoundException {

        if (fechaPartida == null) {
            return this.vueloService.obtenerTodosLosVuelosSinFecha(origen, destino);
        }
        return this.vueloService.obtenerTodosLosVuelosConFecha(origen, destino, fechaPartida);
    }

    @Operation(summary = "Programar vuelos vuelos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "el vuelo se guardo con exito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VueloModel.class))})
    })
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path = "/vuelo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> crearVuelo(@RequestBody @Valid VueloModel vuelo){
        return this.vueloService.crearVuelo(vuelo);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "el vuelo se actualizo con exito", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "el vuelo no se encuentra registrado",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "actualizar o modificar un vuelo")
    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping(path="vuelo/{idVuelo}")
    public ResponseEntity<Object> actualizarVuelo(@PathVariable("idVuelo") Long idVuelo, @RequestBody VueloModel editVuelo) throws EntityNotFoundException {
        return this.vueloService.actualizarVuelo(idVuelo, editVuelo);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "el vuelo se elimino con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "El vuelo no se encuantra programado",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "eliminar vuelo por id")
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping(path = "vuelo/{idVuelo}")
    public ResponseEntity<Object> eliminarVueloPorId(@PathVariable("idVuelo") Long idVuelo) throws EntityNotFoundException {
        return this.vueloService.eliminarVueloPorId(idVuelo);
    }
}
