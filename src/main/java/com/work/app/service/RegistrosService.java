package com.work.app.service;

import com.work.app.domain.Registros;
import com.work.app.repository.RegistrosRepository;
import com.work.app.service.dto.RegistrosDTO;
import com.work.app.service.mapper.RegistrosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Registros}.
 */
@Service
@Transactional
public class RegistrosService {

    private final Logger log = LoggerFactory.getLogger(RegistrosService.class);

    private final RegistrosRepository registrosRepository;

    private final RegistrosMapper registrosMapper;

    public RegistrosService(RegistrosRepository registrosRepository, RegistrosMapper registrosMapper) {
        this.registrosRepository = registrosRepository;
        this.registrosMapper = registrosMapper;
    }

    /**
     * Save a registros.
     *
     * @param registrosDTO the entity to save.
     * @return the persisted entity.
     */
    public RegistrosDTO save(RegistrosDTO registrosDTO) {
        log.debug("Request to save Registros : {}", registrosDTO);
        Registros registros = registrosMapper.toEntity(registrosDTO);
        registros = registrosRepository.save(registros);
        return registrosMapper.toDto(registros);
    }

    /**
     * Update a registros.
     *
     * @param registrosDTO the entity to save.
     * @return the persisted entity.
     */
    public RegistrosDTO update(RegistrosDTO registrosDTO) {
        log.debug("Request to update Registros : {}", registrosDTO);
        Registros registros = registrosMapper.toEntity(registrosDTO);
        registros = registrosRepository.save(registros);
        return registrosMapper.toDto(registros);
    }

    /**
     * Partially update a registros.
     *
     * @param registrosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RegistrosDTO> partialUpdate(RegistrosDTO registrosDTO) {
        log.debug("Request to partially update Registros : {}", registrosDTO);

        return registrosRepository
            .findById(registrosDTO.getId())
            .map(existingRegistros -> {
                registrosMapper.partialUpdate(existingRegistros, registrosDTO);

                return existingRegistros;
            })
            .map(registrosRepository::save)
            .map(registrosMapper::toDto);
    }

    /**
     * Get all the registros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RegistrosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Registros");
        return registrosRepository.findAll(pageable).map(registrosMapper::toDto);
    }

    /**
     * Get one registros by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegistrosDTO> findOne(Long id) {
        log.debug("Request to get Registros : {}", id);
        return registrosRepository.findById(id).map(registrosMapper::toDto);
    }

    /**
     * Delete the registros by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Registros : {}", id);
        registrosRepository.deleteById(id);
    }
}
