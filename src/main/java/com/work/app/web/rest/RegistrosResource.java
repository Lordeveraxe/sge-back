package com.work.app.web.rest;

import com.work.app.repository.RegistrosRepository;
import com.work.app.service.RegistrosService;
import com.work.app.service.dto.RegistrosDTO;
import com.work.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.work.app.domain.Registros}.
 */
@RestController
@RequestMapping("/api")
public class RegistrosResource {

    private final Logger log = LoggerFactory.getLogger(RegistrosResource.class);

    private static final String ENTITY_NAME = "registros";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistrosService registrosService;

    private final RegistrosRepository registrosRepository;

    public RegistrosResource(RegistrosService registrosService, RegistrosRepository registrosRepository) {
        this.registrosService = registrosService;
        this.registrosRepository = registrosRepository;
    }

    /**
     * {@code POST  /registros} : Create a new registros.
     *
     * @param registrosDTO the registrosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registrosDTO, or with status {@code 400 (Bad Request)} if the registros has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registros")
    public ResponseEntity<RegistrosDTO> createRegistros(@RequestBody RegistrosDTO registrosDTO) throws URISyntaxException {
        log.debug("REST request to save Registros : {}", registrosDTO);
        if (registrosDTO.getId() != null) {
            throw new BadRequestAlertException("A new registros cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistrosDTO result = registrosService.save(registrosDTO);
        return ResponseEntity
            .created(new URI("/api/registros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registros/:id} : Updates an existing registros.
     *
     * @param id the id of the registrosDTO to save.
     * @param registrosDTO the registrosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registrosDTO,
     * or with status {@code 400 (Bad Request)} if the registrosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registrosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registros/{id}")
    public ResponseEntity<RegistrosDTO> updateRegistros(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistrosDTO registrosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Registros : {}, {}", id, registrosDTO);
        if (registrosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registrosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registrosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegistrosDTO result = registrosService.update(registrosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registrosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /registros/:id} : Partial updates given fields of an existing registros, field will ignore if it is null
     *
     * @param id the id of the registrosDTO to save.
     * @param registrosDTO the registrosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registrosDTO,
     * or with status {@code 400 (Bad Request)} if the registrosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the registrosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the registrosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/registros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegistrosDTO> partialUpdateRegistros(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistrosDTO registrosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Registros partially : {}, {}", id, registrosDTO);
        if (registrosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registrosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registrosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistrosDTO> result = registrosService.partialUpdate(registrosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registrosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /registros} : get all the registros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registros in body.
     */
    @GetMapping("/registros")
    public ResponseEntity<List<RegistrosDTO>> getAllRegistros(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Registros");
        Page<RegistrosDTO> page = registrosService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registros/:id} : get the "id" registros.
     *
     * @param id the id of the registrosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registrosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registros/{id}")
    public ResponseEntity<RegistrosDTO> getRegistros(@PathVariable Long id) {
        log.debug("REST request to get Registros : {}", id);
        Optional<RegistrosDTO> registrosDTO = registrosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registrosDTO);
    }

    /**
     * {@code DELETE  /registros/:id} : delete the "id" registros.
     *
     * @param id the id of the registrosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> deleteRegistros(@PathVariable Long id) {
        log.debug("REST request to delete Registros : {}", id);
        registrosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
