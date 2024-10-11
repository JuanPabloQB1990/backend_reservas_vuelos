package com.proyecto.reservaVuelos.mappers;

import com.proyecto.reservaVuelos.dto.ClienteModelDto;
import com.proyecto.reservaVuelos.models.ClienteModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClienteMapper {

//    @Mappings({
//            @Mapping(source = "codigoVuelo", target = "codVuelo"),
//            @Mapping(source = "tipoVuelo.nombre", target = "tipoVuelo"),
//            @Mapping(source = "aerolinea.nombre", target = "aerolinea")
//
//    })
    ClienteModelDto toClienteDto(ClienteModel cliente);

}
