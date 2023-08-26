package com.work.app.service.mapper;

import com.work.app.domain.Campos;
import com.work.app.domain.Contadores;
import com.work.app.domain.Variables;
import com.work.app.service.dto.CamposDTO;
import com.work.app.service.dto.ContadoresDTO;
import com.work.app.service.dto.VariablesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contadores} and its DTO {@link ContadoresDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContadoresMapper extends EntityMapper<ContadoresDTO, Contadores> {
    @Mapping(target = "variables", source = "variables", qualifiedByName = "variablesId")
    @Mapping(target = "campo", source = "campo", qualifiedByName = "camposId")
    ContadoresDTO toDto(Contadores s);

    @Named("variablesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VariablesDTO toDtoVariablesId(Variables variables);

    @Named("camposId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CamposDTO toDtoCamposId(Campos campos);
}
