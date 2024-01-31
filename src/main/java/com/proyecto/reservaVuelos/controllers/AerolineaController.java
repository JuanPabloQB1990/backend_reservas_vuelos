package com.proyecto.reservaVuelos.controllers;

import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.services.AerolineaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/aerolineas")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET})
@Tag(name = "Aerolinea", description = "Catálogo de aerolineas")
public class AerolineaController {
    private AerolineaService aerolineaService;

    @Autowired
    public AerolineaController(AerolineaService aerolineaService) {
        this.aerolineaService = aerolineaService;
    }

    //@CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public List<AerolineaModel> getAerolineas(){
        return this.aerolineaService.getAerolineas();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    public void agregarAerolinea(@RequestBody @Valid AerolineaModel aerolinea){
        this.aerolineaService.saveAerolinea(aerolinea);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> editarAerolinea(@PathVariable Long id, @RequestBody AerolineaModel editAerolinea) throws EntityNotFoundException {
        return this.aerolineaService.editarAerolinea(id, editAerolinea);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> eliminarAerolinea(@PathVariable Long id) throws EntityNotFoundException {
        return this.aerolineaService.eliminarAerolinea(id);
    }
}
