package com.work.app.web.rest;

import com.work.app.repository.CamposRepository;
import com.work.app.service.CamposService;
import com.work.app.service.dto.CamposDTO;
import com.work.app.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
 * REST controller for managing {@link com.work.app.domain.Campos}.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class CamposResource {

    private final Logger log = LoggerFactory.getLogger(CamposResource.class);

    private static final String ENTITY_NAME = "campos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CamposService camposService;

    private final CamposRepository camposRepository;

    public CamposResource(CamposService camposService, CamposRepository camposRepository) {
        this.camposService = camposService;
        this.camposRepository = camposRepository;
    }

    /**
     * {@code POST  /campos} : Create a new campos.
     *
     * @param camposDTO the camposDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new camposDTO, or with status {@code 400 (Bad Request)} if the campos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campos")
    public ResponseEntity<CamposDTO> createCampos(@RequestBody CamposDTO camposDTO) throws URISyntaxException {
        log.debug("REST request to save Campos : {}", camposDTO);
        if (camposDTO.getId() != null) {
            throw new BadRequestAlertException("A new campos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CamposDTO result = camposService.save(camposDTO);
        return ResponseEntity
            .created(new URI("/api/campos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campos/:id} : Updates an existing campos.
     *
     * @param id the id of the camposDTO to save.
     * @param camposDTO the camposDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated camposDTO,
     * or with status {@code 400 (Bad Request)} if the camposDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the camposDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campos/{id}")
    public ResponseEntity<CamposDTO> updateCampos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CamposDTO camposDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Campos : {}, {}", id, camposDTO);
        if (camposDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, camposDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!camposRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CamposDTO result = camposService.update(camposDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, camposDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /campos/:id} : Partial updates given fields of an existing campos, field will ignore if it is null
     *
     * @param id the id of the camposDTO to save.
     * @param camposDTO the camposDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated camposDTO,
     * or with status {@code 400 (Bad Request)} if the camposDTO is not valid,
     * or with status {@code 404 (Not Found)} if the camposDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the camposDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/campos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CamposDTO> partialUpdateCampos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CamposDTO camposDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Campos partially : {}, {}", id, camposDTO);
        if (camposDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, camposDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!camposRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CamposDTO> result = camposService.partialUpdate(camposDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, camposDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /campos} : get all the campos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campos in body.
     */
    @GetMapping("/campos")
    public ResponseEntity<List<CamposDTO>> getAllCampos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Campos");
        Page<CamposDTO> page = camposService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campos/:id} : get the "id" campos.
     *
     * @param id the id of the camposDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the camposDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campos/{id}")
    public ResponseEntity<CamposDTO> getCampos(@PathVariable Long id) {
        log.debug("REST request to get Campos : {}", id);
        Optional<CamposDTO> camposDTO = camposService.findOne(id);
        return ResponseUtil.wrapOrNotFound(camposDTO);
    }

    /**
     * {@code DELETE  /campos/:id} : delete the "id" campos.
     *
     * @param id the id of the camposDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campos/{id}")
    public ResponseEntity<Void> deleteCampos(@PathVariable Long id) {
        log.debug("REST request to delete Campos : {}", id);
        camposService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
