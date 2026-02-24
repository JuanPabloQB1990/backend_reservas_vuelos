package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.dto.VueloUpdateDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.services.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("api/vuelos")
@Tag(name = "Vuelos", description = "Catálogo de vuelos")
@SecurityRequirement(name = "javainuseapi")
public class VueloController {

    private VueloService vueloService;

    // *********************************************************** OBTENER VUELO POR ID ***************************************************** //
    @Autowired
    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = VueloModelDto.class))),
            @ApiResponse(responseCode = "404", description = "vuelo no encontrado",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "Obtener vuelo por id", security = {@SecurityRequirement(name= "BearerJWT")})
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="vuelo/{idVuelo}")
    public VueloModelDto obtenerVueloPorId(@PathVariable("idVuelo") Long idVuelo) throws EntityNotFoundException {
        return this.vueloService.obtenerVueloPorId(idVuelo);
    }

    // *********************************************************** OBTENER TODOS LOS VUELOS ***************************************************** //

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "no hay vuelos programados",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "Obtener todos los vuelos", security = {@SecurityRequirement(name= "BearerJWT")})
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path = "all")
    public Page<VueloModelDto> obtenerTodosLosVuelos(Pageable pageable) throws EntityNotFoundException {
        return this.vueloService.obtenerTodosLosVuelos(pageable);
    }

    // *********************************************************** OBTENER VUELOS POR ORIGEN - DESTINO - FECHA ***************************************************** //

    @Operation(summary = "Obtener vuelos por fecha, pasajeros, origen y destino", security = {@SecurityRequirement(name= "BearerJWT")})
    @ApiResponse(responseCode = "404", description = "no hay vuelos programados", content = @Content)
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarVuelos(

            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fecha,
            @RequestParam int pasajeros
    ) throws EntityNotFoundException {

        LocalDateTime fechaParseada = LocalDate.parse(fecha).atStartOfDay();

        List<?> vuelos = vueloService.buscarVuelosConEscalas(
                origen,
                destino,
                fechaParseada,
                pasajeros
        );

        return ResponseEntity.ok(vuelos);
    }

    // *********************************************************** CREAR VUELOS ***************************************************** //
    @Operation(summary = "Programar vuelos (los vuelos se programan automaticamente al arrancar el proyecto)", security = {@SecurityRequirement(name= "BearerJWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Los vuelos se programaron con exito", content = @Content)
    })
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path = "vuelo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> crearVuelo() throws EntityNotFoundException {
        return this.vueloService.verificarVuelosAlInicio();
    }

    // *********************************************************** ACTUALIZAR VUELOS POR ID ***************************************************** //

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "el vuelo se actualizo con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "el vuelo no se encuentra registrado",content = @Content)
    })
    @Operation(summary = "Actualizar parcialmente un vuelo", security = {@SecurityRequirement(name= "BearerJWT")})
    @CrossOrigin(origins = "http://localhost:5173")
    @PatchMapping(path="vuelo/{idVuelo}")
    public ResponseEntity<Object> actualizarVuelo(@PathVariable("idVuelo") Long idVuelo, @RequestBody @Valid VueloUpdateDto editVuelo) throws EntityNotFoundException {
        return this.vueloService.actualizarVuelo(idVuelo, editVuelo);
    }

 // *************************************** ELIMINAR VUELO POR ID ****************************************** //

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "el vuelo se elimino con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "El vuelo no se encuantra programado", content = @Content)
    })
    @Operation(summary = "Eliminar vuelo por id", security = {@SecurityRequirement(name= "BearerJWT")})
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping(path = "vuelo/{idVuelo}")
    public ResponseEntity<Object> eliminarVueloPorId(@PathVariable("idVuelo") Long idVuelo) throws EntityNotFoundException {
        return this.vueloService.eliminarVueloPorId(idVuelo);
    }



}
