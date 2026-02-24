package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.dto.AuthRespuestaDto;
import com.proyecto.reservaVuelos.dto.LoginDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Autenticar cliente",
            description = "Permite a un cliente iniciar sesión con username y password. " +
                    "Si las credenciales son correctas, retorna un JWT para acceder a los endpoints protegidos."
    )

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRespuestaDto.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                  "rol": "CLIENTE",
                                  "idCliente": 15
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "error": "Credenciales inválidas"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "error": "El usuario no se encuentra registrado"
                                }
                                """
                            )
                    )
            )
    })
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "login")
    public ResponseEntity<AuthRespuestaDto> loguearCliente(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales del cliente", required = true, content = @Content(schema = @Schema(implementation = LoginDto.class),
                    examples = @ExampleObject(
                            value = """
                                        {
                                          "username": "juanpablo",
                                          "password": "123456"
                                        }
                                    """
                    )
            )
    )@RequestBody LoginDto loginDto) throws EntityNotFoundException {
        return this.clienteService.loguearCliente(loginDto);
    }


    @Operation(
            summary = "Registrar nuevo cliente",
            description = "Crea un nuevo cliente en el sistema. " +
                    "El username debe ser único. La contraseña será almacenada de forma encriptada."
    )

    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente registrado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "message": "registro satisfactorio"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "error": "Datos inválidos"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "El usuario ya existe",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "error": "Este cliente ya se encuentra registrado"
                                }
                                """
                            )
                    )
            )
    })
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "registro")
    public ResponseEntity<Object> registrarCliente(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del cliente a registrar",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ClienteModel.class),
                    examples = @ExampleObject(
                            value = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Quintero",
                                  "telefono": "3001234567",
                                  "correo": "juan@email.com",
                                  "username": "juanpq",
                                  "password": "123456",
                                  "pais": "Colombia",
                                  "ciudad": "Medellin",
                                  "direccion": "Calle 10 #20-30"
                                }
                                """
                    )
            )
    )@RequestBody ClienteModel cliente) throws EntityNotFoundException {
        return this.clienteService.registrarCliente(cliente);
    }

}
