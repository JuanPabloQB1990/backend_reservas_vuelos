package com.proyecto.reservaVuelos.mappers;

import com.proyecto.reservaVuelos.dto.VueloModelDto;
import com.proyecto.reservaVuelos.models.VueloModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VueloMapper{

    @Mappings({
            @Mapping(source = "codigoVuelo", target = "codVuelo"),
            @Mapping(source = "tipoVuelo", target = "tipoVuelo"),
            @Mapping(source = "aerolinea", target = "aerolinea"),
            @Mapping(source = "origenId", target = "origen"),
            @Mapping(source = "destinoId", target = "destino")

    })
    VueloModelDto toVueloDto(VueloModel vuelo);
}
