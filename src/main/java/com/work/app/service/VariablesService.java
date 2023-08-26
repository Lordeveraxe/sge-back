package com.work.app.service;

import com.work.app.domain.Variables;
import com.work.app.repository.VariablesRepository;
import com.work.app.service.dto.VariablesDTO;
import com.work.app.service.mapper.VariablesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Variables}.
 */
@Service
@Transactional
public class VariablesService {

    private final Logger log = LoggerFactory.getLogger(VariablesService.class);

    private final VariablesRepository variablesRepository;

    private final VariablesMapper variablesMapper;

    public VariablesService(VariablesRepository variablesRepository, VariablesMapper variablesMapper) {
        this.variablesRepository = variablesRepository;
        this.variablesMapper = variablesMapper;
    }

    /**
     * Save a variables.
     *
     * @param variablesDTO the entity to save.
     * @return the persisted entity.
     */
    public VariablesDTO save(VariablesDTO variablesDTO) {
        log.debug("Request to save Variables : {}", variablesDTO);
        Variables variables = variablesMapper.toEntity(variablesDTO);
        variables = variablesRepository.save(variables);
        return variablesMapper.toDto(variables);
    }

    /**
     * Update a variables.
     *
     * @param variablesDTO the entity to save.
     * @return the persisted entity.
     */
    public VariablesDTO update(VariablesDTO variablesDTO) {
        log.debug("Request to update Variables : {}", variablesDTO);
        Variables variables = variablesMapper.toEntity(variablesDTO);
        variables = variablesRepository.save(variables);
        return variablesMapper.toDto(variables);
    }

    /**
     * Partially update a variables.
     *
     * @param variablesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VariablesDTO> partialUpdate(VariablesDTO variablesDTO) {
        log.debug("Request to partially update Variables : {}", variablesDTO);

        return variablesRepository
            .findById(variablesDTO.getId())
            .map(existingVariables -> {
                variablesMapper.partialUpdate(existingVariables, variablesDTO);

                return existingVariables;
            })
            .map(variablesRepository::save)
            .map(variablesMapper::toDto);
    }

    /**
     * Get all the variables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VariablesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Variables");
        return variablesRepository.findAll(pageable).map(variablesMapper::toDto);
    }

    /**
     * Get one variables by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VariablesDTO> findOne(Long id) {
        log.debug("Request to get Variables : {}", id);
        return variablesRepository.findById(id).map(variablesMapper::toDto);
    }

    /**
     * Delete the variables by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Variables : {}", id);
        variablesRepository.deleteById(id);
    }
}
