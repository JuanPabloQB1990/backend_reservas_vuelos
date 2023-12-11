package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.EscalaModelList;
import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class VueloService {

    private VueloRepository vueloRepository;
    private VueloMapper vueloMapper;

    @Autowired
    public VueloService(VueloRepository vueloRepository, VueloMapper vueloMapper) {
        this.vueloRepository = vueloRepository;
        this.vueloMapper = vueloMapper;
    }

    private HashMap<String, Object> datos;

    public VueloModelDto obtenerVueloPorId(Long idVuelo) throws EntityNotFoundException {
        Optional<VueloModel> vueloEncontrado = this.vueloRepository.findById(idVuelo);

        if(vueloEncontrado.isPresent()){
            return VueloModelDto.build(
                    vueloEncontrado.get().getIdVuelo(),
                    vueloEncontrado.get().getCodigoVuelo(),
                    vueloEncontrado.get().getOrigen(),
                    vueloEncontrado.get().getDestino(),
                    vueloEncontrado.get().getFechaPartida(),
                    vueloEncontrado.get().getFechaLlegada(),
                    vueloEncontrado.get().getPrecio(),
                    vueloEncontrado.get().getAsientos(),
                    vueloEncontrado.get().getTipoVuelo().getNombre(),
                    vueloEncontrado.get().getAerolinea().getNombre()
            );

        }else{
            throw new EntityNotFoundException("Vuelo no encontrado", HttpStatusCode.valueOf(404));
        }
    }

    public Page<VueloModelDto> obtenerTodosLosVuelos(Pageable pageable) throws EntityNotFoundException {

        Page<VueloModel> vuelos = this.vueloRepository.findAll(pageable);

        if (vuelos.getTotalElements() > 0){
            Page<VueloModelDto> vuelosDto = vuelos
                    .map(vuelo -> vueloMapper.toVueloDto(vuelo));

            return vuelosDto;
        }else{
            throw new EntityNotFoundException("no hay vuelos programados", HttpStatusCode.valueOf(404));
        }

    }

    public Stack<List<VueloModelDto>> obtenerTodosLosVuelosConFecha(String origen, String destino, LocalDate fechaPartida) throws EntityNotFoundException {

        Stack<List<VueloModelDto>> escalas = new Stack<>();

        List<VueloModelDto> listaVuelos = new ArrayList<>();

        List<VueloModel> vuelosDirectosConFecha =  vueloRepository.buscarVuelosDirectosConFecha(origen, destino, fechaPartida);

        if (!vuelosDirectosConFecha.isEmpty()){
            for (VueloModel vueloDirecto: vuelosDirectosConFecha) {
                listaVuelos.add(vueloMapper.toVueloDto(vueloDirecto));

            }

            escalas.add(new EscalaModelList(listaVuelos).getVuelos());
        }

        // buscar vuelos con solo origen -> primeros vuelos
        List<VueloModel> primerosVuelos = vueloRepository.buscarVuelosConSoloOrigenConFecha(origen, fechaPartida);

        // guardar vuelos con una escala
        for (VueloModel primerVuelo: primerosVuelos) {

            // buscar vuelos segundo con destino
            List<VueloModel> segundoVueloUnaEscala = vueloRepository.buscarSegundoVueloUnaEscala(primerVuelo.getDestino(), destino, primerVuelo.getFechaLlegada());

            if (!segundoVueloUnaEscala.isEmpty()){

                for (VueloModel segundoVuelo: segundoVueloUnaEscala) {
                    if (segundoVuelo.getFechaPartida().minusHours(1).isAfter(primerVuelo.getFechaLlegada())){
                        listaVuelos.add(vueloMapper.toVueloDto(primerVuelo));
                        listaVuelos.add(vueloMapper.toVueloDto(segundoVuelo));

                        EscalaModelList escala = new EscalaModelList(listaVuelos);
                        escalas.push(escala.getVuelos());
                        escala.setVuelos(listaVuelos = new ArrayList<>());
                    }

                }
            }

            List<VueloModel> vuelosTerceros = vueloRepository.buscarTercerVueloDosEscalas(destino, primerVuelo.getFechaLlegada());

            if (!vuelosTerceros.isEmpty()) {

                for (VueloModel vueloTercero : vuelosTerceros) {

                    // buscar vuelos intermedios
                    List<VueloModel> vuelosIntermedios = vueloRepository.buscarVuelosIntermidiosDosEscalas(primerVuelo.getDestino(),vueloTercero.getOrigen(), primerVuelo.getFechaLlegada(), vueloTercero.getFechaPartida());

                    if (!vuelosIntermedios.isEmpty()) {

                        for (VueloModel vueloIntermedio :vuelosIntermedios) {

                            if (vueloTercero.getFechaPartida().minusHours(1).isAfter(vueloIntermedio.getFechaLlegada())){
                                listaVuelos.add(vueloMapper.toVueloDto(primerVuelo));
                                listaVuelos.add(vueloMapper.toVueloDto(vueloIntermedio));
                                listaVuelos.add(vueloMapper.toVueloDto(vueloTercero));

                                EscalaModelList escala = new EscalaModelList(listaVuelos);
                                escalas.push(escala.getVuelos());
                                escala.setVuelos(listaVuelos = new ArrayList<>());
                            }

                        }
                    }

                }
            }
        }

        if (!escalas.isEmpty()){
            return escalas;
        }else {
            throw new EntityNotFoundException("no hay vuelos programados", HttpStatusCode.valueOf(404));
        }
    }

    public Stack<List<VueloModelDto>> obtenerTodosLosVuelosSinFecha(String origen, String destino) throws EntityNotFoundException {

        Stack<List<VueloModelDto>> escalas = new Stack<>();
        ArrayList<VueloModelDto> listaVuelos = new ArrayList<>();

        //buscar vuelos directos
        List<VueloModel> vuelosDirectosSinFecha = vueloRepository.buscarVuelosDirectosSinFecha(origen, destino);

        if (!vuelosDirectosSinFecha.isEmpty()){
            for (VueloModel vueloDirecto: vuelosDirectosSinFecha) {
                listaVuelos.add(vueloMapper.toVueloDto(vueloDirecto));

                EscalaModelList escala = new EscalaModelList(listaVuelos);
                escalas.push(escala.getVuelos());
                escala.setVuelos(listaVuelos = new ArrayList<>());
            }

        }

        // buscar vuelos con solo origen -> primeros vuelos
        List<VueloModel> primerosVuelos = vueloRepository.buscarVuelosConSoloOrigenSinFecha(origen);

        // guardar vuelos con una escala
        for (VueloModel primerVuelo: primerosVuelos) {

            // buscar vuelos segundo con destino
            List<VueloModel> segundoVueloUnaEscala = vueloRepository.buscarSegundoVueloUnaEscala(primerVuelo.getDestino(), destino, primerVuelo.getFechaLlegada());

            if (!segundoVueloUnaEscala.isEmpty()){

                for (VueloModel segundoVuelo: segundoVueloUnaEscala) {
                    if (segundoVuelo.getFechaPartida().minusHours(1).isAfter(primerVuelo.getFechaLlegada())){

                        listaVuelos.add(vueloMapper.toVueloDto(primerVuelo));
                        listaVuelos.add(vueloMapper.toVueloDto(segundoVuelo));

                        EscalaModelList escala = new EscalaModelList(listaVuelos);
                        escalas.push(escala.getVuelos());
                        escala.setVuelos(listaVuelos = new ArrayList<>());
                    }

                }
            }

            List<VueloModel> vuelosTerceros = vueloRepository.buscarTercerVueloDosEscalas(destino, primerVuelo.getFechaLlegada());

            if (!vuelosTerceros.isEmpty()) {

                for (VueloModel vueloTercero : vuelosTerceros) {

                // buscar vuelos intermedios
                    List<VueloModel> vuelosIntermedios = vueloRepository.buscarVuelosIntermidiosDosEscalas(primerVuelo.getDestino(),vueloTercero.getOrigen(), primerVuelo.getFechaLlegada(), vueloTercero.getFechaPartida());

                    if (!vuelosIntermedios.isEmpty()) {

                        for (VueloModel vueloIntermedio :vuelosIntermedios) {

                            if (vueloTercero.getFechaPartida().minusHours(1).isAfter(vueloIntermedio.getFechaLlegada()) && vueloIntermedio.getFechaPartida().minusHours(1).isAfter(primerVuelo.getFechaLlegada())){

                                listaVuelos.add(vueloMapper.toVueloDto(primerVuelo));
                                listaVuelos.add(vueloMapper.toVueloDto(vueloIntermedio));
                                listaVuelos.add(vueloMapper.toVueloDto(vueloTercero));

                                EscalaModelList escala = new EscalaModelList(listaVuelos);
                                escalas.push(escala.getVuelos());
                                escala.setVuelos(listaVuelos = new ArrayList<>());
                            }

                        }
                    }

                }
            }
        }

        if (!escalas.isEmpty()){
            return escalas;
        }else {
            throw new EntityNotFoundException("no hay vuelos programados", HttpStatusCode.valueOf(404));
        }

    }

    public ResponseEntity<Object> crearVuelo(VueloModel vuelo){

        this.vueloRepository.crearVuelo(
                vuelo.getOrigen(),
                vuelo.getDestino(),
                vuelo.getFechaPartida(),
                vuelo.getFechaLlegada(),
                vuelo.getPrecio(),
                vuelo.getAsientos(),
                vuelo.getTipoVuelo().getIdTipoVuelo(),
                vuelo.getAerolinea().getIdAerolinea());

        datos = new HashMap<>();
        datos.put("message", "el vuelo se guardo con exito");

        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Object> actualizarVuelo(Long idVuelo, VueloModel editVuelo) throws EntityNotFoundException {

        datos = new HashMap<>();

        Optional<VueloModel> vueloEncontrado = vueloRepository.findById(idVuelo);

        if (vueloEncontrado.isPresent()){
            if (editVuelo.getAerolinea().getIdAerolinea() == vueloEncontrado.get().getAerolinea().getIdAerolinea()){
                vueloEncontrado.get().setCodigoVuelo(editVuelo.getCodigoVuelo());
                vueloEncontrado.get().setOrigen(editVuelo.getOrigen());
                vueloEncontrado.get().setDestino(editVuelo.getDestino());
                vueloEncontrado.get().setFechaPartida(editVuelo.getFechaPartida());
                vueloEncontrado.get().setFechaLlegada(editVuelo.getFechaLlegada());
                vueloEncontrado.get().setPrecio(editVuelo.getPrecio());
                vueloEncontrado.get().setAsientos(editVuelo.getAsientos());
                vueloEncontrado.get().setTipoVuelo(editVuelo.getTipoVuelo());
                vueloEncontrado.get().setAerolinea(editVuelo.getAerolinea());
                this.vueloRepository.save(vueloEncontrado.get());
            }else{
                vueloRepository.actualizarVuelo(
                        idVuelo,
                        editVuelo.getOrigen(),
                        editVuelo.getDestino(),
                        editVuelo.getFechaPartida(),
                        editVuelo.getFechaLlegada(),
                        editVuelo.getPrecio(),
                        editVuelo.getAsientos(),
                        editVuelo.getTipoVuelo().getIdTipoVuelo(),
                        editVuelo.getAerolinea().getIdAerolinea());
            }

            datos.put("message", "el vuelo se actualizo con exito");

            return new ResponseEntity<>(
                    datos,
                    HttpStatus.OK
            );
        }else{
            throw new EntityNotFoundException("el vuelo no se encuentra registrado", HttpStatusCode.valueOf(404));
        }
    }

    public ResponseEntity<Object> eliminarVueloPorId(Long idVuelo) throws EntityNotFoundException {
        Optional<VueloModel> vueloEncontrado = this.vueloRepository.findById(idVuelo);

        if (vueloEncontrado.isPresent()) {
            this.vueloRepository.deleteById(idVuelo);
            datos = new HashMap<>();
            datos.put("message", "el vuelo se elimino con exito");
            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(204)
            );
        }else{
            throw new EntityNotFoundException("El vuelo no se encuantra programado", HttpStatusCode.valueOf(404));
        }

    }



}
