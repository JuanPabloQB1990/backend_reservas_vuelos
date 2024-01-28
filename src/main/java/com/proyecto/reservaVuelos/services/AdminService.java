package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.CambiarRollDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.models.RolModel;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AdminService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public AdminService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteModel obtenerClientePorId(Long idCliente){
        return this.clienteRepository.findById(idCliente).get();
    }

    public ResponseEntity<Object> modificarRolUsuario(CambiarRollDto rol, Long idCliente) throws EntityNotFoundException {

        Optional<ClienteModel> cliente = this.clienteRepository.findById(idCliente);
        if (cliente.isPresent()){
            if (rol.getRol().equals(RolModel.ADMIN.name())){
                cliente.get().setRol(RolModel.ADMIN);
            }
            if (rol.getRol().equalsIgnoreCase(RolModel.EMPLEADO.name())){
                cliente.get().setRol(RolModel.EMPLEADO);
            }
            if (rol.getRol().equalsIgnoreCase(RolModel.CLIENTE.name())){
                cliente.get().setRol(RolModel.CLIENTE);
            }

            this.clienteRepository.save(cliente.get());

            HashMap<String, String> respuesta = new HashMap<>();
            respuesta.put("message", "el rol del usuario fue modificado satisfactoriamente");
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }

        throw new EntityNotFoundException("el usuario no esta registrado");
    }
}
