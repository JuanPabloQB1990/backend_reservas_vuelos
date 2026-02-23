package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.CambiarRollDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;
import com.proyecto.reservaVuelos.services.AdminService;
//import com.proyecto.reservaVuelos.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/admin")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET})
@Tag(name = "administrador", description = "Catálogo para administrar roles de usuario")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "no hay Usuarios registrados",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "Obtener Usuario por ID", security = {@SecurityRequirement(name= "BearerJWT")})
    @GetMapping(path = "cliente/{idCliente}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ClienteModel obtenerCliente(@PathVariable Long idCliente){
        return this.adminService.obtenerClientePorId(idCliente);
    }

    @PutMapping(path = "cliente/{idCliente}")
    public ResponseEntity<Object> modificarRolUsuario(@RequestBody CambiarRollDto rol, @PathVariable Long idCliente) throws EntityNotFoundException {
        return this.adminService.modificarRolUsuario(rol, idCliente);
    }
}
