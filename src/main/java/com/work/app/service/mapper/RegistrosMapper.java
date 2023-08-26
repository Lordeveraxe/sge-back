package com.work.app.service.mapper;

import com.work.app.domain.Contadores;
import com.work.app.domain.Registros;
import com.work.app.service.dto.ContadoresDTO;
import com.work.app.service.dto.RegistrosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Registros} and its DTO {@link RegistrosDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegistrosMapper extends EntityMapper<RegistrosDTO, Registros> {
    @Mapping(target = "contadores", source = "contadores", qualifiedByName = "contadoresId")
    RegistrosDTO toDto(Registros s);

    @Named("contadoresId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContadoresDTO toDtoContadoresId(Contadores contadores);
}
