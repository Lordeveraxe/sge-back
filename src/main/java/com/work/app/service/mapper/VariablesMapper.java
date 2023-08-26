package com.work.app.service.mapper;

import com.work.app.domain.Variables;
import com.work.app.service.dto.VariablesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Variables} and its DTO {@link VariablesDTO}.
 */
@Mapper(componentModel = "spring")
public interface VariablesMapper extends EntityMapper<VariablesDTO, Variables> {}
