package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.EscalaModelList;
import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.dto.VueloUpdateDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import com.proyecto.reservaVuelos.repositories.AeropuertoRepository;
import com.proyecto.reservaVuelos.repositories.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VueloService {

    private VueloRepository vueloRepository;
    private VueloMapper vueloMapper;
    private final AeropuertoRepository aeropuertoRepository;
    private final AerolineaRepository aerolineaRepository;

    @Autowired
    public VueloService(VueloRepository vueloRepository, VueloMapper vueloMapper, AeropuertoRepository aeropuertoRepository, AerolineaRepository aerolineaRepository) {
        this.vueloRepository = vueloRepository;
        this.vueloMapper = vueloMapper;
        this.aeropuertoRepository = aeropuertoRepository;
        this.aerolineaRepository = aerolineaRepository;
    }

    private HashMap<String, Object> datos;

    private static final int MAX_ESCALAS = 3;

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

    // METODO AUXILIAR

    private void buscarConexiones(
            String ciudadActual,
            String destinoFinal,
            LocalDateTime llegadaAnterior,
            List<VueloModel> rutaActual,
            List<List<VueloModel>> resultados,
            int escalas,
            int pasajeros,
            String paisOrigen,
            boolean salioDelPais
    ) {

        if (escalas > MAX_ESCALAS) return;

        if (ciudadActual.equalsIgnoreCase(destinoFinal)) {
            resultados.add(new ArrayList<>(rutaActual));
            return;
        }

        LocalDateTime minConexion = llegadaAnterior.plusMinutes(45);
        LocalDateTime maxConexion = llegadaAnterior.plusHours(3);

        List<VueloModel> siguientes =
                vueloRepository.buscarConexionesValidas(
                        ciudadActual,
                        minConexion,
                        maxConexion,
                        pasajeros
                );

        for (VueloModel siguiente : siguientes) {

            String ciudadSiguiente = siguiente.getDestino().getCiudad();
            String paisSiguiente = siguiente.getDestino().getPais();

            // evitar repetir ciudad
            boolean ciudadVisitada = rutaActual.stream()
                    .anyMatch(v ->
                            v.getOrigen().getCiudad()
                                    .equalsIgnoreCase(ciudadSiguiente)
                                    ||
                                    v.getDestino().getCiudad()
                                            .equalsIgnoreCase(ciudadSiguiente)
                    );

            if (ciudadVisitada) continue;

            // evitar salir del país y volver
            if (salioDelPais && paisSiguiente.equalsIgnoreCase(paisOrigen)) {
                continue;
            }

            if (escalas >= 2 && paisSiguiente.equalsIgnoreCase(paisOrigen)) {
                continue;
            }

            boolean nuevoSalioDelPais =
                    salioDelPais || !paisSiguiente.equalsIgnoreCase(paisOrigen);

            rutaActual.add(siguiente);

            buscarConexiones(
                    ciudadSiguiente,
                    destinoFinal,
                    siguiente.getFechaLlegada(),
                    rutaActual,
                    resultados,
                    escalas + 1,
                    pasajeros,
                    paisOrigen,
                    nuevoSalioDelPais
            );

            rutaActual.remove(rutaActual.size() - 1);
        }
    }

    public List<List<VueloModelDto>> buscarVuelosConEscalas(
            String origen,
            String destino,
            LocalDateTime fecha,
            int pasajeros
    ) {

        List<List<VueloModel>> resultados = new ArrayList<>();

        LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        List<VueloModel> primerosVuelos =
                vueloRepository.buscarPrimerTramo(
                        origen,
                        inicioDia,
                        finDia,
                        pasajeros
                );

        for (VueloModel vuelo : primerosVuelos) {

            List<VueloModel> rutaActual = new ArrayList<>();
            rutaActual.add(vuelo);

            String paisOrigen = vuelo.getOrigen().getPais();

            boolean salioDelPais =
                    !vuelo.getDestino().getPais()
                            .equalsIgnoreCase(paisOrigen);

            buscarConexiones(
                    vuelo.getDestino().getCiudad(),
                    destino,
                    vuelo.getFechaLlegada(),
                    rutaActual,
                    resultados,
                    0,
                    pasajeros,
                    paisOrigen,
                    salioDelPais
            );
        }

        // MAPEAR A DTO
        return resultados.stream()
                .map(ruta -> ruta.stream()
                        .map(vueloMapper::toVueloDto)
                        .toList())
                .toList();
    }
    // ************************************* Crear y Actualizar vuelos ************************************************************************************

    @Scheduled(fixedDelay = 1800000) // 30 minutos
    public ResponseEntity<Object> verificarVuelos() {

        long total = vueloRepository.count();

        if (total == 0) {
            return crearVuelo();
        } else {
            return actualizarVuelosExpirados();
        }
    }

    public ResponseEntity<Object> verificarVuelosAlInicio() {

        long total = vueloRepository.count();

        if (total == 0) {
            return crearVuelo();
        } else {
            return actualizarVuelosExpirados();
        }
    }

    public ResponseEntity<Object> crearVuelo() {

        Random random = new Random();

        List<AeropuertoModel> aeropuertos = aeropuertoRepository.findAll();

        List<AeropuertoModel> hubs = aeropuertos.stream()
                .limit(8)
                .toList();

        int totalVuelos = 8000;

        for (int i = 0; i < totalVuelos; i++) {

            AeropuertoModel origen;
            AeropuertoModel destino;

            int tipoRuta = random.nextInt(4);

            // HUB -> HUB
            if (tipoRuta == 0) {

                origen = hubs.get(random.nextInt(hubs.size()));

                do {
                    destino = hubs.get(random.nextInt(hubs.size()));
                } while (origen.equals(destino));
            }

            // HUB -> ciudad
            else if (tipoRuta == 1) {

                origen = hubs.get(random.nextInt(hubs.size()));

                do {
                    destino = aeropuertos.get(random.nextInt(aeropuertos.size()));
                } while (origen.equals(destino));
            }

            // ciudad -> HUB
            else if (tipoRuta == 2) {

                destino = hubs.get(random.nextInt(hubs.size()));

                do {
                    origen = aeropuertos.get(random.nextInt(aeropuertos.size()));
                } while (origen.equals(destino));
            }

            // ciudad -> ciudad
            else {

                origen = aeropuertos.get(random.nextInt(aeropuertos.size()));

                do {
                    destino = aeropuertos.get(random.nextInt(aeropuertos.size()));
                } while (origen.equals(destino));
            }

            // día del vuelo
            int dias = random.nextInt(25);

            // bancos de conexiones
            int banco = random.nextInt(4);

            int horaBase;

            switch (banco) {
                case 0 -> horaBase = 6;
                case 1 -> horaBase = 10;
                case 2 -> horaBase = 14;
                default -> horaBase = 18;
            }

            LocalDateTime salida = LocalDateTime.now()
                    .plusDays(dias)
                    .withHour(horaBase + random.nextInt(2))
                    .withMinute(random.nextInt(60));

            int duracion = 1 + random.nextInt(8);

            LocalDateTime llegada = salida.plusHours(duracion);

            Long tipoVueloId = random.nextBoolean() ? 1L : 2L;

            Long aerolineaId = (long) (random.nextInt(7) + 1);

            double precio;

            if (tipoVueloId == 1)
                precio = 60 + random.nextInt(120);
            else
                precio = 220 + random.nextInt(700);

            int asientos = 90 + random.nextInt(180);

            vueloRepository.crearVuelo(
                    origen.getIdAeropuerto(),
                    destino.getIdAeropuerto(),
                    salida,
                    llegada,
                    precio,
                    asientos,
                    tipoVueloId,
                    aerolineaId
            );

            // crear vuelo de regreso (muy importante)
            if (random.nextBoolean()) {

                LocalDateTime regresoSalida = llegada.plusHours(1 + random.nextInt(3));
                LocalDateTime regresoLlegada = regresoSalida.plusHours(duracion);

                vueloRepository.crearVuelo(
                        destino.getIdAeropuerto(),
                        origen.getIdAeropuerto(),
                        regresoSalida,
                        regresoLlegada,
                        precio,
                        asientos,
                        tipoVueloId,
                        aerolineaId
                );
            }
        }

        return ResponseEntity.ok("Vuelos generados correctamente");
    }

    @Transactional
    public ResponseEntity<Object> actualizarVuelosExpirados() {

        LocalDateTime ahora = LocalDateTime.now();

        List<VueloModel> expirados =
                vueloRepository.vuelosExpirados(ahora);

        for (VueloModel vuelo : expirados) {

            VueloModel nuevoVuelo = new VueloModel();

            nuevoVuelo.setCodigoVuelo(vuelo.getCodigoVuelo());
            nuevoVuelo.setOrigen(vuelo.getOrigen());
            nuevoVuelo.setDestino(vuelo.getDestino());

            nuevoVuelo.setFechaPartida(
                    vuelo.getFechaPartida().plusMonths(1)
            );

            nuevoVuelo.setFechaLlegada(
                    vuelo.getFechaLlegada().plusMonths(1)
            );

            nuevoVuelo.setPrecio(vuelo.getPrecio());
            nuevoVuelo.setAsientos(vuelo.getAsientos());
            nuevoVuelo.setAerolinea(vuelo.getAerolinea());
            nuevoVuelo.setTipoVuelo(vuelo.getTipoVuelo());

            vueloRepository.delete(vuelo);

            vueloRepository.save(nuevoVuelo);
        }

        return ResponseEntity.ok("Vuelos actualizados");
    }

    // ******************************************************************* ACTUALIZAR VUELO POR ID *********************************************************** //

    public ResponseEntity<Object> actualizarVuelo(Long idVuelo, VueloUpdateDto editVuelo) throws EntityNotFoundException {

        Map<String, Object> datos = new HashMap<>();

        VueloModel vuelo = vueloRepository.findById(idVuelo)
                .orElseThrow(() -> new EntityNotFoundException("El vuelo no existe"));

        if (editVuelo.getCodigoVuelo() != null) {
            vuelo.setCodigoVuelo(editVuelo.getCodigoVuelo());
        }

        if (editVuelo.getOrigenId() != null) {
            AeropuertoModel origen = aeropuertoRepository.findById(editVuelo.getOrigenId())
                    .orElseThrow(() -> new EntityNotFoundException("Aeropuerto origen no existe"));
            vuelo.setOrigen(origen);
        }

        if (editVuelo.getDestinoId() != null) {
            AeropuertoModel destino = aeropuertoRepository.findById(editVuelo.getDestinoId())
                    .orElseThrow(() -> new EntityNotFoundException("Aeropuerto destino no existe"));
            vuelo.setDestino(destino);
        }

        if (editVuelo.getFechaPartida() != null) {
            vuelo.setFechaPartida(editVuelo.getFechaPartida());
        }

        if (editVuelo.getFechaLlegada() != null) {
            vuelo.setFechaLlegada(editVuelo.getFechaLlegada());
        }

        if (editVuelo.getPrecio() != null) {
            vuelo.setPrecio(editVuelo.getPrecio());
        }

        if (editVuelo.getAsientos() != null) {
            vuelo.setAsientos(editVuelo.getAsientos());
        }

        if (editVuelo.getTipoVueloId() != null) {
            VueloModel tipo = vueloRepository.findById(editVuelo.getTipoVueloId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo vuelo no existe"));
            vuelo.setTipoVuelo(tipo.getTipoVuelo());
        }

        if (editVuelo.getAerolineaId() != null) {
            AerolineaModel aerolinea = aerolineaRepository.findById(editVuelo.getAerolineaId())
                    .orElseThrow(() -> new EntityNotFoundException("Aerolinea no existe"));
            vuelo.setAerolinea(aerolinea);
        }

        vueloRepository.save(vuelo);

        datos.put("message", "El vuelo se actualizó correctamente");
        datos.put("vuelo", vuelo);

        return new ResponseEntity<>(datos, HttpStatus.OK);
    }

    // ******************************************************************* ELIMINAR VUELO POR ID *********************************************************** //

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




}
