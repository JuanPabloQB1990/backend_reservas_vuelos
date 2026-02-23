package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.CrearReservaDto;
import com.proyecto.reservaVuelos.dto.ReservacionModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.services.ReservacionService;
import com.proyecto.reservaVuelos.services.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

//    @Operation(summary = "Realizar reservas", security = {@SecurityRequirement(name= "BearerJWT")})
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "La reserva se realizo con exito",
//                    content = { @Content(mediaType = "application/json",
//                        schema = @Schema(implementation = CrearReservaDto.class))}),
//            @ApiResponse(responseCode = "404", description = "El cliente no esta registrado", content = @Content),
//            @ApiResponse(responseCode = "406", description = "No hay suficientes asientos disponibles para el vuelo ",
//                    content = @Content),
//            @ApiResponse(responseCode = "406", description = "La reserva debe realizarse con al menos 3 horas de anticipación.",
//                    content = @Content)})
//    @PostMapping(path = "/reservacion")
//    @CrossOrigin(origins = "http://localhost:5173")
//    public ResponseEntity<Object> crearReservacion(@RequestBody CrearReservaDto reserva) throws EntityNotFoundException {
//        return reservacionService.crearReservacion(reserva);
//    }
//
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "La reserva se elimino con exito", content = @Content),
//            @ApiResponse(responseCode = "404", description = "No se encontraron reservaciones para eliminar",
//                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
//    })
//    @Operation(summary = "Cancelar reserva por codigo de reserva", security = {@SecurityRequirement(name= "BearerJWT")})
//    @DeleteMapping("/reservacion/{codigo}")
//    @CrossOrigin(origins = "http://localhost:5173")
//    public ResponseEntity<Object> cancelarReservacionPorId(@PathVariable(name = "codigo") String codigo) throws EntityNotFoundException {
//        return this.reservacionService.cancelarReservacionPorId(codigo);
//    }

    @Operation(summary = "Obtener reserva por id de reserva", security = {@SecurityRequirement(name= "BearerJWT")})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @GetMapping(path = "/reservacion/{codigoReserva}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ReservacionModelDto obtenerReservaPorId(@PathVariable(name = "codigoReserva") String codigoReserva) throws EntityNotFoundException {
        return this.reservacionService.obtenerReservacionPorId(codigoReserva);
    }

    @Operation(summary = "Obtener reservas de un cliente por id del cliente", security = {@SecurityRequirement(name= "BearerJWT")})
    @ApiResponse(responseCode = "201", description = "La reserva se realizo con exito")
    @ApiResponse(responseCode = "404", description = "El cliente no tiene reservaciones", content = @Content)
    @ApiResponse(responseCode = "401", description = "El cliente no esta registrado", content = @Content)
    @GetMapping(path = "/cliente/{idCliente}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<ReservacionModelDto> obtenerReservacionesPorIdCliente(@PathVariable(name = "idCliente") Long idCliente) throws EntityNotFoundException {
        return this.reservacionService.obtenerReservacionesPorIdCliente(idCliente);
    }

}
