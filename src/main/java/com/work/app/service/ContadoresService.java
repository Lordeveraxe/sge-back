package com.work.app.service;

import com.work.app.domain.Contadores;
import com.work.app.repository.ContadoresRepository;
import com.work.app.service.dto.ContadoresDTO;
import com.work.app.service.mapper.ContadoresMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contadores}.
 */
@Service
@Transactional
public class ContadoresService {

    private final Logger log = LoggerFactory.getLogger(ContadoresService.class);

    private final ContadoresRepository contadoresRepository;

    private final ContadoresMapper contadoresMapper;

    public ContadoresService(ContadoresRepository contadoresRepository, ContadoresMapper contadoresMapper) {
        this.contadoresRepository = contadoresRepository;
        this.contadoresMapper = contadoresMapper;
    }

    /**
     * Save a contadores.
     *
     * @param contadoresDTO the entity to save.
     * @return the persisted entity.
     */
    public ContadoresDTO save(ContadoresDTO contadoresDTO) {
        log.debug("Request to save Contadores : {}", contadoresDTO);
        Contadores contadores = contadoresMapper.toEntity(contadoresDTO);
        contadores = contadoresRepository.save(contadores);
        return contadoresMapper.toDto(contadores);
    }

    /**
     * Update a contadores.
     *
     * @param contadoresDTO the entity to save.
     * @return the persisted entity.
     */
    public ContadoresDTO update(ContadoresDTO contadoresDTO) {
        log.debug("Request to update Contadores : {}", contadoresDTO);
        Contadores contadores = contadoresMapper.toEntity(contadoresDTO);
        contadores = contadoresRepository.save(contadores);
        return contadoresMapper.toDto(contadores);
    }

    /**
     * Partially update a contadores.
     *
     * @param contadoresDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContadoresDTO> partialUpdate(ContadoresDTO contadoresDTO) {
        log.debug("Request to partially update Contadores : {}", contadoresDTO);

        return contadoresRepository
            .findById(contadoresDTO.getId())
            .map(existingContadores -> {
                contadoresMapper.partialUpdate(existingContadores, contadoresDTO);

                return existingContadores;
            })
            .map(contadoresRepository::save)
            .map(contadoresMapper::toDto);
    }

    /**
     * Get all the contadores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContadoresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contadores");
        return contadoresRepository.findAll(pageable).map(contadoresMapper::toDto);
    }

    /**
     * Get one contadores by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContadoresDTO> findOne(Long id) {
        log.debug("Request to get Contadores : {}", id);
        return contadoresRepository.findById(id).map(contadoresMapper::toDto);
    }

    /**
     * Delete the contadores by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contadores : {}", id);
        contadoresRepository.deleteById(id);
    }
}
