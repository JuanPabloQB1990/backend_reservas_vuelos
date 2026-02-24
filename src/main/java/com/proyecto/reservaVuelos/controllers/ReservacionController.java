package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.CrearReservaDto;
import com.proyecto.reservaVuelos.dto.FacturaReservacionDto;
import com.proyecto.reservaVuelos.dto.ReservacionModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.services.ReservacionService;
import com.proyecto.reservaVuelos.services.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservaciones")
@Tag(name = "Reservacion", description = "Catálogo de reservaciones")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@SecurityRequirement(name = "javainuseapi")
//@Api(value = "Usuarios", description = "Endpoints entidad Reserva")
public class ReservacionController {

    private ReservacionService reservacionService;
    private VueloService vueloService;

    @Autowired
    public ReservacionController(ReservacionService reservacionService, VueloService vueloService) {
        this.reservacionService = reservacionService;
        this.vueloService = vueloService;
    }


    @Operation(
            summary = "Crear una reservación de vuelos",
            description = "Permite a un cliente reservar uno o varios vuelos disponibles. "
                    + "Valida disponibilidad de asientos, tiempo mínimo antes del vuelo "
                    + "y genera una factura con el total a pagar.",
            security = {@SecurityRequirement(name= "BearerJWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reservación creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FacturaReservacionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o asientos insuficientes"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente o vuelo no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @PostMapping(path = "/reservacion")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<FacturaReservacionDto> crearReservacion(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para crear la reservación",
            required = true,
            content = @Content(schema = @Schema(implementation = CrearReservaDto.class))
    )@Valid @RequestBody CrearReservaDto reserva) throws EntityNotFoundException {
        return this.reservacionService.crearReservacion(reserva);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "La reserva se elimino con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservaciones para eliminar",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "Cancelar reserva por Id de Reserva", security = {@SecurityRequirement(name= "BearerJWT")})
    @DeleteMapping("/reservacion/{codigo}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Object> cancelarReservacionPorId(@PathVariable(name = "codigo") Long idReservacion) throws EntityNotFoundException {
        return this.reservacionService.cancelarReservacionPorId(idReservacion);
    }


    @Operation(
            summary = "Obtener reservaciones de un cliente",
            description = "Retorna la lista de reservaciones realizadas por un cliente junto con "
                    + "los vuelos asociados a cada reservación. Si el cliente no existe se devuelve un error.",
            security = {@SecurityRequirement(name= "BearerJWT")}
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Reservaciones encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservacionModelDto.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Persona no Registrada"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping(path = "/cliente/{idCliente}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<ReservacionModelDto>> obtenerReservacionesPorIdCliente(@Parameter(
            description = "ID del cliente del cual se desean consultar las reservaciones",
            example = "3",
            required = true
    )@PathVariable(name = "idCliente") Long idCliente) throws EntityNotFoundException {
        return this.reservacionService.obtenerReservacionesPorIdCliente(idCliente);
    }

}
