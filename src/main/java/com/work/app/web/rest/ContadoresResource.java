package com.work.app.web.rest;

import com.work.app.repository.ContadoresRepository;
import com.work.app.service.ContadoresService;
import com.work.app.service.dto.ContadoresDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.work.app.domain.Contadores}.
 */
@RestController
@RequestMapping("/api")
public class ContadoresResource {

    private final Logger log = LoggerFactory.getLogger(ContadoresResource.class);

    private static final String ENTITY_NAME = "contadores";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContadoresService contadoresService;

    private final ContadoresRepository contadoresRepository;

    public ContadoresResource(ContadoresService contadoresService, ContadoresRepository contadoresRepository) {
        this.contadoresService = contadoresService;
        this.contadoresRepository = contadoresRepository;
    }

    /**
     * {@code POST  /contadores} : Create a new contadores.
     *
     * @param contadoresDTO the contadoresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contadoresDTO, or with status {@code 400 (Bad Request)} if the contadores has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contadores")
    public ResponseEntity<ContadoresDTO> createContadores(@RequestBody ContadoresDTO contadoresDTO) throws URISyntaxException {
        log.debug("REST request to save Contadores : {}", contadoresDTO);
        if (contadoresDTO.getId() != null) {
            throw new BadRequestAlertException("A new contadores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContadoresDTO result = contadoresService.save(contadoresDTO);
        return ResponseEntity
            .created(new URI("/api/contadores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contadores/:id} : Updates an existing contadores.
     *
     * @param id the id of the contadoresDTO to save.
     * @param contadoresDTO the contadoresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contadoresDTO,
     * or with status {@code 400 (Bad Request)} if the contadoresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contadoresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contadores/{id}")
    public ResponseEntity<ContadoresDTO> updateContadores(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContadoresDTO contadoresDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contadores : {}, {}", id, contadoresDTO);
        if (contadoresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contadoresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contadoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContadoresDTO result = contadoresService.update(contadoresDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contadoresDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contadores/:id} : Partial updates given fields of an existing contadores, field will ignore if it is null
     *
     * @param id the id of the contadoresDTO to save.
     * @param contadoresDTO the contadoresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contadoresDTO,
     * or with status {@code 400 (Bad Request)} if the contadoresDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contadoresDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contadoresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contadores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContadoresDTO> partialUpdateContadores(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContadoresDTO contadoresDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contadores partially : {}, {}", id, contadoresDTO);
        if (contadoresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contadoresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contadoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContadoresDTO> result = contadoresService.partialUpdate(contadoresDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contadoresDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contadores} : get all the contadores.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contadores in body.
     */
    @GetMapping("/contadores")
    public ResponseEntity<List<ContadoresDTO>> getAllContadores(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contadores");
        Page<ContadoresDTO> page = contadoresService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contadores/:id} : get the "id" contadores.
     *
     * @param id the id of the contadoresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contadoresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contadores/{id}")
    public ResponseEntity<ContadoresDTO> getContadores(@PathVariable Long id) {
        log.debug("REST request to get Contadores : {}", id);
        Optional<ContadoresDTO> contadoresDTO = contadoresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contadoresDTO);
    }

    /**
     * {@code DELETE  /contadores/:id} : delete the "id" contadores.
     *
     * @param id the id of the contadoresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contadores/{id}")
    public ResponseEntity<Void> deleteContadores(@PathVariable Long id) {
        log.debug("REST request to delete Contadores : {}", id);
        contadoresService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
