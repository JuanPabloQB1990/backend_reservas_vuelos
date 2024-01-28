package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AerolineaService {

    private AerolineaRepository aerolineaRepository;

    @Autowired
    public AerolineaService(AerolineaRepository aerolineaRepository) {
        this.aerolineaRepository = aerolineaRepository;
    }

    private HashMap<String, Object> datos;

    public List<AerolineaModel> getAerolineas(){
        return this.aerolineaRepository.findAll();
    }

    public void saveAerolinea(AerolineaModel aerolinea){
        this.aerolineaRepository.save(aerolinea);
    }

    public ResponseEntity<Object> editarAerolinea(Long id, AerolineaModel editAerolinea) throws EntityNotFoundException {
        Optional<AerolineaModel> aerolineaEncontrada = this.aerolineaRepository.findById(id);

        if (aerolineaEncontrada.isPresent()){
            aerolineaEncontrada.get().setNombre(editAerolinea.getNombre());
            this.aerolineaRepository.save(aerolineaEncontrada.get());

            datos = new HashMap<>();
            datos.put("message", "la aerolinea se actualizo con exito");

            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(201)
            );
        }else{
            throw new EntityNotFoundException("la aerolinea no se encuentra registrada");
        }
    }

    public ResponseEntity<Object> eliminarAerolinea(Long id) throws EntityNotFoundException {
        Optional<AerolineaModel> vueloEncontrado = this.aerolineaRepository.findById(id);

        if (vueloEncontrado.isPresent()){
            this.aerolineaRepository.deleteById(id);
            datos = new HashMap<>();
            datos.put("message", "la aerolinea se elimino con exito");
            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(204)
            );
        }else{
            throw new EntityNotFoundException("El vuelo no se encuantra programado");
        }
    }
}
