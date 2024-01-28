package com.proyecto.reservaVuelos.services;


import com.proyecto.reservaVuelos.dto.AuthRespuestaDto;
import com.proyecto.reservaVuelos.dto.LoginDto;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.models.RolModel;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;

import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final JwtService jwtService;


    public ResponseEntity<Object> registrarCliente(ClienteModel cliente) throws EntityNotFoundException {

        if (clienteRepository.existsByUsername(cliente.getUsername())){
            throw new EntityNotFoundException("Este cliente ya se encuentra registrado");
        }

        ClienteModel clienteRegistrado = new ClienteModel();
        clienteRegistrado.setNombre(cliente.getNombre());
        clienteRegistrado.setApellido(cliente.getApellido());
        clienteRegistrado.setTelefono(cliente.getTelefono());
        clienteRegistrado.setCorreo(cliente.getCorreo());
        clienteRegistrado.setUsername(cliente.getUsername());
        clienteRegistrado.setPassword(passwordEncoder.encode(cliente.getPassword()));
        clienteRegistrado.setPais(cliente.getPais());
        clienteRegistrado.setCiudad(cliente.getCiudad());
        clienteRegistrado.setDireccion(cliente.getDireccion());
        clienteRegistrado.setRol(RolModel.CLIENTE);
        this.clienteRepository.save(clienteRegistrado);

        HashMap<String, Object> datos = new HashMap<>();
        datos.put("message", "registro  satisfactorio");

        return new ResponseEntity(datos, HttpStatusCode.valueOf(201));
    }

    public ResponseEntity<AuthRespuestaDto> loguearCliente(LoginDto loginDto) throws EntityNotFoundException {
        Optional<ClienteModel> cliente = this.clienteRepository.findByUsername(loginDto.getUsername());
        if (cliente.isPresent()){
            UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            authenticationManager.authenticate(userNamePassword);
            UserDetails user = clienteRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User with username " + loginDto.getUsername() + " not found"));
            String token = jwtService.getToken(user);
            return new ResponseEntity<>(new AuthRespuestaDto(token, cliente.get().getRol(), cliente.get().getIdCliente()), HttpStatus.OK);
        }

        throw new EntityNotFoundException("el usuario no se encuentra registrado");

    }
}

