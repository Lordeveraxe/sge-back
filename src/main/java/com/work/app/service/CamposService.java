package com.work.app.service;

import com.work.app.domain.Campos;
import com.work.app.repository.CamposRepository;
import com.work.app.service.dto.CamposDTO;
import com.work.app.service.mapper.CamposMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Campos}.
 */
@Service
@Transactional
public class CamposService {

    private final Logger log = LoggerFactory.getLogger(CamposService.class);

    private final CamposRepository camposRepository;

    private final CamposMapper camposMapper;

    public CamposService(CamposRepository camposRepository, CamposMapper camposMapper) {
        this.camposRepository = camposRepository;
        this.camposMapper = camposMapper;
    }

    /**
     * Save a campos.
     *
     * @param camposDTO the entity to save.
     * @return the persisted entity.
     */
    public CamposDTO save(CamposDTO camposDTO) {
        log.debug("Request to save Campos : {}", camposDTO);
        Campos campos = camposMapper.toEntity(camposDTO);
        campos = camposRepository.save(campos);
        return camposMapper.toDto(campos);
    }

    /**
     * Update a campos.
     *
     * @param camposDTO the entity to save.
     * @return the persisted entity.
     */
    public CamposDTO update(CamposDTO camposDTO) {
        log.debug("Request to update Campos : {}", camposDTO);
        Campos campos = camposMapper.toEntity(camposDTO);
        campos = camposRepository.save(campos);
        return camposMapper.toDto(campos);
    }

    /**
     * Partially update a campos.
     *
     * @param camposDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CamposDTO> partialUpdate(CamposDTO camposDTO) {
        log.debug("Request to partially update Campos : {}", camposDTO);

        return camposRepository
            .findById(camposDTO.getId())
            .map(existingCampos -> {
                camposMapper.partialUpdate(existingCampos, camposDTO);

                return existingCampos;
            })
            .map(camposRepository::save)
            .map(camposMapper::toDto);
    }

    /**
     * Get all the campos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CamposDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campos");
        return camposRepository.findAll(pageable).map(camposMapper::toDto);
    }

    /**
     * Get one campos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CamposDTO> findOne(Long id) {
        log.debug("Request to get Campos : {}", id);
        return camposRepository.findById(id).map(camposMapper::toDto);
    }

    /**
     * Delete the campos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Campos : {}", id);
        camposRepository.deleteById(id);
    }
}
