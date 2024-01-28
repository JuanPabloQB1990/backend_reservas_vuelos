package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.TipoVueloModel;
import com.proyecto.reservaVuelos.repositories.TipoVueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class TipoVueloService {

    private TipoVueloRepository tipoVueloRepository;

    @Autowired
    public TipoVueloService(TipoVueloRepository tipoVueloRepository) {
        this.tipoVueloRepository = tipoVueloRepository;
    }

    private HashMap<String, Object> datos;

    public List<TipoVueloModel> getTipoVuelos(){
        return this.tipoVueloRepository.findAll();
    }

    public void saveTipoVuelo(TipoVueloModel tipoVuelo){
        this.tipoVueloRepository.save(tipoVuelo);
    }

    public ResponseEntity<Object> editarTipoVuelo(Long id, TipoVueloModel editTipoVuelo) throws EntityNotFoundException {
        Optional<TipoVueloModel> tipoVueloEncontrado = this.tipoVueloRepository.findById(id);

        if (tipoVueloEncontrado.isPresent()){
            tipoVueloEncontrado.get().setNombre(editTipoVuelo.getNombre());
            this.tipoVueloRepository.save(tipoVueloEncontrado.get());

            datos = new HashMap<>();
            datos.put("message", "el tipo de vuelo se actualizo con exito");

            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(201)
            );
        }else{
            throw new EntityNotFoundException("el tipo de vuelo no se encuentra registrada");
        }
    }

    public ResponseEntity<Object> eliminarTipoVuelo(Long id) throws EntityNotFoundException {
        Optional<TipoVueloModel> vueloEncontrado = this.tipoVueloRepository.findById(id);

        if (vueloEncontrado.isPresent()){
            this.tipoVueloRepository.deleteById(id);
            datos = new HashMap<>();
            datos.put("message", "el tipo de vuelo se elimino con exito");
            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(204)
            );
        }else{
            throw new EntityNotFoundException("El vuelo no se encuantra programado");
        }
    }
}
