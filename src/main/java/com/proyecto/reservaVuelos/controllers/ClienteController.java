package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.AuthRespuestaDto;
import com.proyecto.reservaVuelos.dto.LoginDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/auth")
@Tag(name = "Cliente", description = "Catálogo de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "login")
    public ResponseEntity<AuthRespuestaDto> loguearCliente(@RequestBody LoginDto loginDto) throws EntityNotFoundException {
        return this.clienteService.loguearCliente(loginDto);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "registro")
    public ResponseEntity<Object> registrarCliente(@RequestBody @Valid ClienteModel cliente) throws EntityNotFoundException {
        return this.clienteService.registrarCliente(cliente);
    }

}
