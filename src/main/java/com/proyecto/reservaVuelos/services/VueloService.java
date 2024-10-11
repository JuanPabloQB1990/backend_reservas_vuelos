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
import java.time.LocalDateTime;
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
            VueloModelDto vuelo = vueloMapper.toVueloDto(vueloEncontrado.get());
            return vuelo;
        }else{
            throw new EntityNotFoundException("Vuelo no encontrado");
        }
    }

    public Page<VueloModelDto> obtenerTodosLosVuelos(Pageable pageable) throws EntityNotFoundException {
        System.out.println(pageable);
        Page<VueloModel> vuelos = this.vueloRepository.findAll(pageable);

        if (vuelos.getTotalElements() > 0){
            Page<VueloModelDto> vuelosDto = vuelos
                    .map(vuelo -> vueloMapper.toVueloDto(vuelo));

            return vuelosDto;
        }else{
            throw new EntityNotFoundException("no hay vuelos programados");
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
            throw new EntityNotFoundException("no hay vuelos programados");
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
                    if (segundoVuelo.getFechaPartida().minusHours(1).isAfter(primerVuelo.getFechaLlegada()) && !segundoVuelo.getFechaPartida().minusHours(12).isAfter(primerVuelo.getFechaLlegada())){

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

                            if (vueloTercero.getFechaPartida().minusHours(1).isAfter(vueloIntermedio.getFechaLlegada()) && !vueloTercero.getFechaPartida().minusHours(12).isAfter(vueloIntermedio.getFechaLlegada()) && vueloIntermedio.getFechaPartida().minusHours(1).isAfter(primerVuelo.getFechaLlegada()) && !vueloIntermedio.getFechaPartida().minusHours(12).isAfter(primerVuelo.getFechaLlegada())){

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
            throw new EntityNotFoundException("no hay vuelos programados");
        }

    }

    public ResponseEntity<Object> crearVuelo(VueloModel vuelo){

        for (int i = 0; i <= 29; i+=5) {

            if (i == 0){
                for (int j = 1; j < 7; j+=5) {
                    if (j == 1){
                        this.vueloRepository.crearVuelo(
                                vuelo.getOrigen(),
                                vuelo.getDestino(),
                                vuelo.getFechaPartida(),
                                vuelo.getFechaLlegada(),
                                vuelo.getPrecio(),
                                vuelo.getAsientos(),
                                vuelo.getTipoVuelo().getIdTipoVuelo(),
                                vuelo.getAerolinea().getIdAerolinea());
                    }else{
                        LocalDateTime fechaPartidaConUnaHoraMas = vuelo.getFechaPartida().plusHours(j);
                        LocalDateTime fechaLlegadaConUnaHoraMas = vuelo.getFechaLlegada().plusHours(j);

                        this.vueloRepository.crearVuelo(
                                vuelo.getOrigen(),
                                vuelo.getDestino(),
                                fechaPartidaConUnaHoraMas,
                                fechaLlegadaConUnaHoraMas,
                                vuelo.getPrecio(),
                                vuelo.getAsientos(),
                                vuelo.getTipoVuelo().getIdTipoVuelo(),
                                vuelo.getAerolinea().getIdAerolinea());
                    }
                }

            }else{

                LocalDateTime fechaPartidaConUnDiaMas = vuelo.getFechaPartida().plusDays(i);
                LocalDateTime fechaLlegadaConUnDiaMas = vuelo.getFechaLlegada().plusDays(i);

                for (int j = 1; j < 7; j+=5) {
                    if (j==1){
                        this.vueloRepository.crearVuelo(
                                vuelo.getOrigen(),
                                vuelo.getDestino(),
                                fechaPartidaConUnDiaMas,
                                fechaLlegadaConUnDiaMas,
                                vuelo.getPrecio(),
                                vuelo.getAsientos(),
                                vuelo.getTipoVuelo().getIdTipoVuelo(),
                                vuelo.getAerolinea().getIdAerolinea());
                    }else{
                        LocalDateTime fechaPartidaConUnaHoraMas = fechaLlegadaConUnDiaMas.plusHours(j);
                        LocalDateTime fechaLlegadaConUnaHoraMas = fechaLlegadaConUnDiaMas.plusHours(j);

                        this.vueloRepository.crearVuelo(
                                vuelo.getOrigen(),
                                vuelo.getDestino(),
                                fechaPartidaConUnaHoraMas,
                                fechaLlegadaConUnaHoraMas,
                                vuelo.getPrecio(),
                                vuelo.getAsientos(),
                                vuelo.getTipoVuelo().getIdTipoVuelo(),
                                vuelo.getAerolinea().getIdAerolinea());
                    }

                }

            }

        }

        datos = new HashMap<>();
        datos.put("message", "los vuelos se guardaron con exito");

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
            throw new EntityNotFoundException("el vuelo no se encuentra registrado");
        }
    }

    public ResponseEntity<Object> eliminarVueloPorId(Long idVuelo) throws EntityNotFoundException {
        Optional<VueloModel> vueloEncontrado = this.vueloRepository.findById(idVuelo);

        if (vueloEncontrado.isPresent()) {
            this.vueloRepository.deleteById(vueloEncontrado.get().getIdVuelo());
            datos = new HashMap<>();
            datos.put("message", "el vuelo se elimino con exito");
            datos.put("status", HttpStatusCode.valueOf(204));
            return new ResponseEntity<>(
                    datos,
                    HttpStatusCode.valueOf(204)
            );
        }else{
            throw new EntityNotFoundException("El vuelo no se encuantra programado");
        }

    }

    public ResponseEntity<Object> actualizarFechasVuelos(int num) {
        List<VueloModel> vuelos = this.vueloRepository.findAll();

        for (VueloModel vuelo :vuelos) {
            LocalDateTime fechaPartidaConUnMesMas = vuelo.getFechaPartida().plusMonths(num);
            LocalDateTime fechaLlegadaConUnMesMas = vuelo.getFechaLlegada().plusMonths(num);
            vuelo.setFechaPartida(fechaPartidaConUnMesMas);
            vuelo.setFechaLlegada(fechaLlegadaConUnMesMas);
            this.vueloRepository.save(vuelo);
        }

        return ResponseEntity.accepted().body("vuelos actualizados");

    }


}
