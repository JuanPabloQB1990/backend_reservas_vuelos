package com.proyecto.reservaVuelos.config;

import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.models.AeropuertoModel;
import com.proyecto.reservaVuelos.models.TipoVueloModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import com.proyecto.reservaVuelos.repositories.AeropuertoRepository;
import com.proyecto.reservaVuelos.repositories.TipoVueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final AerolineaRepository aerolineaRepository;
    private final TipoVueloRepository tipoVueloRepository;
    private final AeropuertoRepository aeropuertoRepository;

    @Bean
    CommandLineRunner cargarDatos() {
        return args -> {

            // TIPOS DE VUELO
            if (tipoVueloRepository.count() == 0) {

                tipoVueloRepository.saveAll(List.of(
                        new TipoVueloModel(null, "PUBLICO"),
                        new TipoVueloModel(null, "PRIVADO")
                ));
            }

            // AEROLINEAS
            if (aerolineaRepository.count() == 0) {

                aerolineaRepository.saveAll(List.of(
                        new AerolineaModel(null, "Avianca"),
                        new AerolineaModel(null, "LATAM"),
                        new AerolineaModel(null, "American Airlines"),
                        new AerolineaModel(null, "Delta Airlines"),
                        new AerolineaModel(null, "Iberia"),
                        new AerolineaModel(null, "Air France"),
                        new AerolineaModel(null, "Emirates")
                ));
            }

            // AEROPUERTOS
            if (aeropuertoRepository.count() == 0) {

                aeropuertoRepository.saveAll(List.of(

                        // Colombia
                        new AeropuertoModel("MDE","Medellin","Aeropuerto Internacional Jose Maria Cordova","Colombia"),
                        new AeropuertoModel("BOG","Bogota","Aeropuerto Internacional El Dorado","Colombia"),
                        new AeropuertoModel("CTG","Cartagena","Aeropuerto Internacional Rafael Nunez","Colombia"),
                        new AeropuertoModel("CLO","Cali","Aeropuerto Internacional Alfonso Bonilla Aragon","Colombia"),
                        new AeropuertoModel("BAQ","Barranquilla","Aeropuerto Internacional Ernesto Cortissoz","Colombia"),

                        // USA
                        new AeropuertoModel("JFK","New York","John F. Kennedy International Airport","USA"),
                        new AeropuertoModel("LAX","Los Angeles","Los Angeles International Airport","USA"),
                        new AeropuertoModel("ATL","Atlanta","Hartsfield Jackson Atlanta International Airport","USA"),
                        new AeropuertoModel("MIA","Miami","Miami International Airport","USA"),
                        new AeropuertoModel("ORD","Chicago","O Hare International Airport","USA"),
                        new AeropuertoModel("DFW","Dallas","Dallas Fort Worth International Airport","USA"),
                        new AeropuertoModel("DEN","Denver","Denver International Airport","USA"),
                        new AeropuertoModel("SEA","Seattle","Seattle Tacoma International Airport","USA"),
                        new AeropuertoModel("LAS","Las Vegas","Harry Reid International Airport","USA"),
                        new AeropuertoModel("SFO","San Francisco","San Francisco International Airport","USA"),

                        // Mexico
                        new AeropuertoModel("MEX","Ciudad de Mexico","Aeropuerto Internacional Benito Juarez","Mexico"),
                        new AeropuertoModel("CUN","Cancun","Aeropuerto Internacional de Cancun","Mexico"),
                        new AeropuertoModel("GDL","Guadalajara","Aeropuerto Internacional Miguel Hidalgo","Mexico"),

                        // Brasil
                        new AeropuertoModel("GRU","Sao Paulo","Sao Paulo Guarulhos International Airport","Brasil"),
                        new AeropuertoModel("GIG","Rio de Janeiro","Rio de Janeiro Galeao International Airport","Brasil"),
                        new AeropuertoModel("BSB","Brasilia","Brasilia International Airport","Brasil"),

                        // Argentina
                        new AeropuertoModel("EZE","Buenos Aires","Ministro Pistarini International Airport","Argentina"),
                        new AeropuertoModel("AEP","Buenos Aires","Aeroparque Jorge Newbery","Argentina"),

                        // Chile
                        new AeropuertoModel("SCL","Santiago","Arturo Merino Benitez International Airport","Chile"),

                        // Peru
                        new AeropuertoModel("LIM","Lima","Jorge Chavez International Airport","Peru"),

                        // España
                        new AeropuertoModel("MAD","Madrid","Adolfo Suarez Madrid Barajas Airport","España"),
                        new AeropuertoModel("BCN","Barcelona","Barcelona El Prat Airport","España"),

                        // Francia
                        new AeropuertoModel("CDG","Paris","Charles de Gaulle Airport","Francia"),
                        new AeropuertoModel("ORY","Paris","Paris Orly Airport","Francia"),

                        // Reino Unido
                        new AeropuertoModel("LHR","London","Heathrow Airport","UK"),
                        new AeropuertoModel("LGW","London","Gatwick Airport","UK"),
                        new AeropuertoModel("MAN","Manchester","Manchester Airport","UK"),

                        // Alemania
                        new AeropuertoModel("FRA","Frankfurt","Frankfurt Airport","Alemania"),
                        new AeropuertoModel("MUC","Munich","Munich Airport","Alemania"),

                        // Italia
                        new AeropuertoModel("FCO","Roma","Leonardo da Vinci Fiumicino Airport","Italia"),
                        new AeropuertoModel("MXP","Milan","Malpensa Airport","Italia"),

                        // Holanda
                        new AeropuertoModel("AMS","Amsterdam","Amsterdam Schiphol Airport","Holanda"),

                        // Medio Oriente
                        new AeropuertoModel("IST","Estambul","Istanbul Airport","Turquia"),
                        new AeropuertoModel("DXB","Dubai","Dubai International Airport","EAU"),
                        new AeropuertoModel("AUH","Abu Dhabi","Abu Dhabi International Airport","EAU"),
                        new AeropuertoModel("DOH","Doha","Hamad International Airport","Qatar"),

                        // Asia
                        new AeropuertoModel("HND","Tokyo","Tokyo Haneda Airport","Japon"),
                        new AeropuertoModel("NRT","Tokyo","Narita International Airport","Japon"),
                        new AeropuertoModel("ICN","Seoul","Incheon International Airport","Corea del Sur"),
                        new AeropuertoModel("PEK","Beijing","Beijing Capital International Airport","China"),
                        new AeropuertoModel("PVG","Shanghai","Shanghai Pudong International Airport","China"),
                        new AeropuertoModel("HKG","Hong Kong","Hong Kong International Airport","China"),
                        new AeropuertoModel("SIN","Singapore","Singapore Changi Airport","Singapore"),
                        new AeropuertoModel("BKK","Bangkok","Suvarnabhumi Airport","Tailandia"),

                        // India
                        new AeropuertoModel("DEL","New Delhi","Indira Gandhi International Airport","India"),
                        new AeropuertoModel("BOM","Mumbai","Chhatrapati Shivaji Maharaj International Airport","India"),

                        // Oceania
                        new AeropuertoModel("SYD","Sydney","Sydney Kingsford Smith Airport","Australia"),
                        new AeropuertoModel("MEL","Melbourne","Melbourne Airport","Australia"),
                        new AeropuertoModel("AKL","Auckland","Auckland Airport","Nueva Zelanda"),

                        // Africa
                        new AeropuertoModel("JNB","Johannesburgo","O R Tambo International Airport","Sudafrica"),
                        new AeropuertoModel("CPT","Cape Town","Cape Town International Airport","Sudafrica"),
                        new AeropuertoModel("CAI","Cairo","Cairo International Airport","Egipto")
                ));
            }

            System.out.println("Datos iniciales cargados correctamente");
        };
    }
}
