package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.ClienteModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.ClienteModel;
import com.proyecto.reservaVuelos.models.RolModel;
import com.proyecto.reservaVuelos.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public AdminService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteModelDto obtenerClientePorId(Long idCliente){
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Persona no registrada"));

        return ClienteModelDto.builder()
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .username(cliente.getUsername())
                .rol(cliente.getRol().name())
                .pais(cliente.getPais())
                .ciudad(cliente.getCiudad())
                .direccion(cliente.getDireccion())
                .reservaciones(cliente.getReservaciones())
                .build();
    }

//    public ResponseEntity<Object> modificarRolUsuario(CambiarRollDto rol, Long idCliente) throws EntityNotFoundException {
//
//        ClienteModel cliente = clienteRepository.findById(idCliente)
//                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
//
//        if (cliente.){
//            if (rol.getRol().equals(RolModel.ADMIN.name())){
//                cliente.get().setRol(RolModel.ADMIN);
//            }
//            if (rol.getRol().equalsIgnoreCase(RolModel.EMPLEADO.name())){
//                cliente.get().setRol(RolModel.EMPLEADO);
//            }
//            if (rol.getRol().equalsIgnoreCase(RolModel.CLIENTE.name())){
//                cliente.get().setRol(RolModel.CLIENTE);
//            }
//
//            this.clienteRepository.save(cliente.get());
//
//            HashMap<String, String> respuesta = new HashMap<>();
//            respuesta.put("message", "el rol del usuario fue modificado satisfactoriamente");
//            return new ResponseEntity<>(respuesta, HttpStatus.OK);
//        }
//
//        throw new EntityNotFoundException("el usuario no esta registrado");
//    }

    @Transactional
    public ClienteModelDto modificarCliente(Long id, ClienteModelDto dto) throws EntityNotFoundException {

        ClienteModel cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no registrada"));

        if (dto.getNombre() != null) {
            cliente.setNombre(dto.getNombre());
        }

        if (dto.getApellido() != null) {
            cliente.setApellido(dto.getApellido());
        }

        if (dto.getTelefono() != null) {
            cliente.setTelefono(dto.getTelefono());
        }

        if (dto.getCorreo() != null) {
            cliente.setCorreo(dto.getCorreo());
        }

        if (dto.getUsername() != null) {
            cliente.setUsername(dto.getUsername());
        }

        if (dto.getPais() != null) {
            cliente.setPais(dto.getPais());
        }

        if (dto.getCiudad() != null) {
            cliente.setCiudad(dto.getCiudad());
        }

        if (dto.getDireccion() != null) {
            cliente.setDireccion(dto.getDireccion());
        }

        if (dto.getRol() != null) {
            cliente.setRol(RolModel.valueOf(dto.getRol()));

        }

        clienteRepository.save(cliente);

        return ClienteModelDto.builder()
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .username(cliente.getUsername())
                .rol(cliente.getRol().name())
                .pais(cliente.getPais())
                .ciudad(cliente.getCiudad())
                .direccion(cliente.getDireccion())
                .reservaciones(cliente.getReservaciones())
                .build();

    }
}
