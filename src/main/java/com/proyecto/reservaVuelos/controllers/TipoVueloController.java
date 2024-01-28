package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.TipoVueloModel;
import com.proyecto.reservaVuelos.services.TipoVueloService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/tipo_vuelos")
//@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET})
@Tag(name = "Tipo de Vuelos", description = "Catálogo de tipo de vuelos")
public class TipoVueloController {

    private TipoVueloService tipoVueloService;

    @Autowired
    public TipoVueloController(TipoVueloService tipoVueloService) {
        this.tipoVueloService = tipoVueloService;
    }

    //@CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public List<TipoVueloModel> getTipoVuelos(){
        return this.tipoVueloService.getTipoVuelos();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    public void agregarTipoVuelo(@RequestBody @Valid TipoVueloModel tipoVuelo){
        this.tipoVueloService.saveTipoVuelo(tipoVuelo);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping(path = "{id}")
    public ResponseEntity<Object> editarTipoVuelo(@PathVariable Long id, @RequestBody TipoVueloModel editTipoVuelo) throws EntityNotFoundException {
        return this.tipoVueloService.editarTipoVuelo(id, editTipoVuelo);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> eliminarTipoVuelo(@PathVariable Long id) throws EntityNotFoundException {
        return this.tipoVueloService.eliminarTipoVuelo(id);
    }
}
