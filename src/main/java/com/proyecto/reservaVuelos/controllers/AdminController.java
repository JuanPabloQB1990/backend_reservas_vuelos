package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.CambiarRollDto;
import com.proyecto.reservaVuelos.dto.ClienteModelDto;
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
@Tag(name = "Administrador", description = "Catálogo para administrar usuarios")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Obtener Usuario por ID", security = {@SecurityRequirement(name= "BearerJWT")})
    @ApiResponse(responseCode = "404", description = "Persona no registrada", content = @Content)
    @GetMapping(path = "cliente/{idCliente}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ClienteModelDto obtenerCliente(@PathVariable Long idCliente){
        return this.adminService.obtenerClientePorId(idCliente);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ClienteModelDto.class))),
            @ApiResponse(responseCode = "404", description = "Persona no registrada",
                    content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @Operation(summary = "Modificar Usuario por ID", security = {@SecurityRequirement(name= "BearerJWT")})
    @PatchMapping("/cliente/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<ClienteModelDto> modificarClienteById(
            @PathVariable Long id,
            @RequestBody ClienteModelDto clienteDto) throws EntityNotFoundException {

        ClienteModelDto clienteActualizado = adminService.modificarCliente(id, clienteDto);

        return ResponseEntity.ok(clienteActualizado);
    }
}
