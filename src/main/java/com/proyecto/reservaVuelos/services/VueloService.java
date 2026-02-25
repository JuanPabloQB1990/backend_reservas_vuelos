package com.proyecto.reservaVuelos.services;

import com.proyecto.reservaVuelos.dto.EscalaModelList;
import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.dto.VueloUpdateDto;
import com.proyecto.reservaVuelos.excepcion.EntityNotFoundException;
import com.proyecto.reservaVuelos.mappers.VueloMapper;
import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.TipoVueloModel;
import com.proyecto.reservaVuelos.models.VueloModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import com.proyecto.reservaVuelos.repositories.AeropuertoRepository;
import com.proyecto.reservaVuelos.repositories.TipoVueloRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VueloService {

    private VueloRepository vueloRepository;
    private VueloMapper vueloMapper;
    private final AeropuertoRepository aeropuertoRepository;
    private final AerolineaRepository aerolineaRepository;
    private final TipoVueloRepository tipoVueloRepository;

    @Autowired
    public VueloService(VueloRepository vueloRepository, VueloMapper vueloMapper, AeropuertoRepository aeropuertoRepository, AerolineaRepository aerolineaRepository, TipoVueloRepository tipoVueloRepository) {
        this.vueloRepository = vueloRepository;
        this.vueloMapper = vueloMapper;
        this.aeropuertoRepository = aeropuertoRepository;
        this.aerolineaRepository = aerolineaRepository;
        this.tipoVueloRepository = tipoVueloRepository;
    }

    private HashMap<String, Object> datos;

    private static final int MAX_ESCALAS = 3;

    private Map<String, Integer> secuenciaAerolinea = new HashMap<>();
    private Set<String> codigosGenerados = new HashSet<>();

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

        if (!vuelos.isEmpty()){
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
    ) throws EntityNotFoundException {

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

        if (!resultados.isEmpty()){

            // MAPEAR A DTO
            return resultados.stream()
                    .map(ruta -> ruta.stream()
                            .map(vueloMapper::toVueloDto)
                            .toList())
                    .toList();
        }else{
            throw new EntityNotFoundException("no hay vuelos programados");
        }


    }
    // ************************************* Crear y Actualizar vuelos ************************************************************************************

    @Scheduled(fixedDelay = 1800000) // 30 minutos
    public ResponseEntity<Object> verificarVuelos() throws EntityNotFoundException {

        long total = vueloRepository.count();

        if (total == 0) {
            return crearVuelos();
        } else {
            return actualizarVuelosExpirados();
        }
    }

    public ResponseEntity<Object> verificarVuelosAlInicio() throws EntityNotFoundException {

        long total = vueloRepository.count();

        if (total == 0) {
            return crearVuelos();
        } else {
            return actualizarVuelosExpirados();
        }
    }

    public synchronized String generarCodigoVueloUnico(String nombreAerolinea) {

        if (nombreAerolinea == null || nombreAerolinea.length() < 2) {
            nombreAerolinea = "XX";
        }

        String prefijo = nombreAerolinea.substring(0, 2).toUpperCase();

        int numero = secuenciaAerolinea.getOrDefault(prefijo, 1000);
        String codigo = prefijo + numero;

        while (codigosGenerados.contains(codigo)) {
            numero++;
            codigo = prefijo + numero;
        }

        secuenciaAerolinea.put(prefijo, numero + 1);
        codigosGenerados.add(codigo);

        return codigo;
    }

    public ResponseEntity<Object> crearVuelos() {

        Random random = new Random();

        List<AeropuertoModel> aeropuertos = aeropuertoRepository.findAll();
        List<AeropuertoModel> hubs = aeropuertos.stream().limit(8).toList();

        List<AerolineaModel> aerolineas = aerolineaRepository.findAll();

        TipoVueloModel nacional = tipoVueloRepository.findById(1L).orElseThrow();
        TipoVueloModel internacional = tipoVueloRepository.findById(2L).orElseThrow();

        int totalVuelos = 8000;

        List<VueloModel> vuelosAGuardar = new ArrayList<>(totalVuelos * 2);

        for (int i = 0; i < totalVuelos; i++) {

            // -------------------------
            // ORIGEN Y DESTINO
            // -------------------------
            AeropuertoModel origen;
            AeropuertoModel destino;

            int tipoRuta = random.nextInt(4);

            switch (tipoRuta) {

                case 0 -> { // HUB → HUB
                    origen = hubs.get(random.nextInt(hubs.size()));
                    do {
                        destino = hubs.get(random.nextInt(hubs.size()));
                    } while (origen.equals(destino));
                }

                case 1 -> { // HUB → ciudad
                    origen = hubs.get(random.nextInt(hubs.size()));
                    do {
                        destino = aeropuertos.get(random.nextInt(aeropuertos.size()));
                    } while (origen.equals(destino));
                }

                case 2 -> { // ciudad → HUB
                    destino = hubs.get(random.nextInt(hubs.size()));
                    do {
                        origen = aeropuertos.get(random.nextInt(aeropuertos.size()));
                    } while (origen.equals(destino));
                }

                default -> { // ciudad → ciudad
                    origen = aeropuertos.get(random.nextInt(aeropuertos.size()));
                    do {
                        destino = aeropuertos.get(random.nextInt(aeropuertos.size()));
                    } while (origen.equals(destino));
                }
            }

            // -------------------------
            // FECHAS (SIEMPRE FUTURO)
            // -------------------------
            LocalDateTime salida = LocalDateTime.now()
                    .plusDays(random.nextInt(30) + 1)
                    .plusHours(random.nextInt(18))
                    .plusMinutes(random.nextInt(60));

            int duracion = 1 + random.nextInt(8);

            LocalDateTime llegada = salida.plusHours(duracion);

            // -------------------------
            // AEROLÍNEA
            // -------------------------
            AerolineaModel aerolinea = aerolineas.get(random.nextInt(aerolineas.size()));

            // -------------------------
            // TIPO VUELO
            // -------------------------
            TipoVueloModel tipoVuelo = random.nextBoolean() ? nacional : internacional;

            // -------------------------
            // PRECIO
            // -------------------------
            double precio = tipoVuelo.getIdTipoVuelo() == 1
                    ? 60 + random.nextInt(120)
                    : 220 + random.nextInt(700);

            int asientos = 90 + random.nextInt(180);

            // -------------------------
            // VUELO IDA
            // -------------------------
            VueloModel vueloIda = new VueloModel();

            vueloIda.setCodigoVuelo(generarCodigoVueloUnico(aerolinea.getNombre()));
            vueloIda.setOrigen(origen);
            vueloIda.setDestino(destino);
            vueloIda.setFechaPartida(salida);
            vueloIda.setFechaLlegada(llegada);
            vueloIda.setPrecio(precio);
            vueloIda.setAsientos(asientos);
            vueloIda.setTipoVuelo(tipoVuelo);
            vueloIda.setAerolinea(aerolinea);

            vuelosAGuardar.add(vueloIda);

            // -------------------------
            // 50% PROBABILIDAD REGRESO
            // -------------------------
            if (random.nextBoolean()) {

                LocalDateTime regresoSalida = llegada.plusHours(1 + random.nextInt(3));
                LocalDateTime regresoLlegada = regresoSalida.plusHours(duracion);

                VueloModel vueloRegreso = new VueloModel();

                vueloRegreso.setCodigoVuelo(generarCodigoVueloUnico(aerolinea.getNombre()));
                vueloRegreso.setOrigen(destino);
                vueloRegreso.setDestino(origen);
                vueloRegreso.setFechaPartida(regresoSalida);
                vueloRegreso.setFechaLlegada(regresoLlegada);
                vueloRegreso.setPrecio(precio);
                vueloRegreso.setAsientos(asientos);
                vueloRegreso.setTipoVuelo(tipoVuelo);
                vueloRegreso.setAerolinea(aerolinea);

                vuelosAGuardar.add(vueloRegreso);
            }
        }

        vueloRepository.saveAll(vuelosAGuardar);

        return ResponseEntity.ok("Los vuelos se programaron con exito: " + vuelosAGuardar.size());
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
