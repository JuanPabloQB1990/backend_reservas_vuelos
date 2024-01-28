package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.CrearReservaDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ReservacionModel;
import com.proyecto.reservaVuelos.services.ReservacionService;
import com.proyecto.reservaVuelos.services.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/reservaciones")
@Tag(name = "Reservacion", description = "Catálogo de reservaciones")
//@Api(value = "Usuarios", description = "Endpoints entidad Reserva")
public class ReservacionController {


    private ReservacionService reservacionService;
    private VueloService vueloService;

    @Autowired
    public ReservacionController(ReservacionService reservacionService, VueloService vueloService) {
        this.reservacionService = reservacionService;
        this.vueloService = vueloService;
    }

    @Operation(summary = "realizar reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "la reserva se realizo con exito",
                    content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CrearReservaDto.class))}),
            @ApiResponse(responseCode = "404", description = "el cliente no esta registrado", content = @Content),
            @ApiResponse(responseCode = "406", description = "No hay suficientes asientos disponibles para el vuelo ",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "La reserva debe realizarse con al menos 3 horas de anticipación.",
                    content = @Content)})
    @PostMapping(path = "/reservacion")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Object> crearReservacion(@RequestBody CrearReservaDto reserva) throws EntityNotFoundException {
        return reservacionService.crearReservacion(reserva);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "la reserva se elimino con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservaciones para eliminar",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "cancelar reserva por id")
    @DeleteMapping("/reservacion/{codigo}")
    public ResponseEntity<Object> cancelarReservacionPorId(@PathVariable(name = "codigo") String codigo) throws EntityNotFoundException {
        return this.reservacionService.cancelarReservacionPorId(codigo);
    }

    @Operation(summary = "obtener reserva por id")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @GetMapping(path = "/reservacion/{idReserva}")
    public ReservacionModel obtenerReservaPorId(@PathVariable Long idReserva){
        return this.reservacionService.obtenerReservacionPorId(idReserva);
    }


    @Operation(summary = "obtener reserva por id del cliente")
    @ApiResponse(responseCode = "201", description = "la reserva se realizo con exito")
    @ApiResponse(responseCode = "404", description = "El cliente no tiene reservaciones", content = @Content)
    @ApiResponse(responseCode = "401", description = "El cliente no esta registrado", content = @Content)
    @GetMapping(path = "/cliente/{idCliente}")
    public List<ReservacionModel> obtenerReservacionesPorIdCliente(@PathVariable Long idCliente) throws EntityNotFoundException {
        return this.reservacionService.obtenerReservacionesPorIdCliente(idCliente);
    }

}
