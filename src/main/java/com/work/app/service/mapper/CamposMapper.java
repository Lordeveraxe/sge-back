package com.work.app.service.mapper;

import com.work.app.domain.Campos;
import com.work.app.service.dto.CamposDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Campos} and its DTO {@link CamposDTO}.
 */
@Mapper(componentModel = "spring")
public interface CamposMapper extends EntityMapper<CamposDTO, Campos> {}
