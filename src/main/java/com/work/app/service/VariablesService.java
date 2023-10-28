package com.work.app.service;

import com.work.app.domain.Variables;
import com.work.app.repository.VariablesRepository;
import com.work.app.service.mapper.VariablesMapper;
import java.util.List;
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

    public VariablesService(VariablesRepository variablesRepository) {
        this.variablesRepository = variablesRepository;
    }

    /**
     * Save a variables.
     *
     * @param variables the entity to save.
     * @return the persisted entity.
     */
    public Variables save(Variables variables) {
        log.debug("Request to save Variables : {}", variables);
        return variablesRepository.save(variables);
    }

    /**
     * Update a variables.
     *
     * @param variables the entity to save.
     * @return the persisted entity.
     */
    public Variables update(Variables variables) {
        log.debug("Request to update Variables : {}", variables);
        return variablesRepository.save(variables);
    }

    /**
     * Partially update a variables.
     *
     * @param variables the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Variables> partialUpdate(Variables variables) {
        log.debug("Request to partially update Variables : {}", variables);

        return variablesRepository
            .findById(variables.getId())
            .map(existingVariables -> {
                if (variables.getVariables() != null) {
                    existingVariables.setVariables(variables.getVariables());
                }

                return existingVariables;
            })
            .map(variablesRepository::save);
    }

    /**
     * Get all the variables.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Variables> findAll() {
        log.debug("Request to get all Variables");
        return variablesRepository.findAll();
    }

    /**
     * Get one variables by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Variables> findOne(Long id) {
        log.debug("Request to get Variables : {}", id);
        return variablesRepository.findById(id);
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
