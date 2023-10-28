package com.work.app.web.rest;

import com.work.app.domain.Variables;
import com.work.app.repository.VariablesRepository;
import com.work.app.service.VariablesService;
import com.work.app.service.dto.VariablesDTO;
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
 * REST controller for managing {@link com.work.app.domain.Variables}.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class VariablesResource {

    private final Logger log = LoggerFactory.getLogger(VariablesResource.class);

    private static final String ENTITY_NAME = "variables";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VariablesService variablesService;

    private final VariablesRepository variablesRepository;

    public VariablesResource(VariablesService variablesService, VariablesRepository variablesRepository) {
        this.variablesService = variablesService;
        this.variablesRepository = variablesRepository;
    }

    /**
     * {@code POST  /variables} : Create a new variables.
     *
     * @param variables the variables to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variables, or with status {@code 400 (Bad Request)} if the variables has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/variables")
    public ResponseEntity<Variables> createVariables(@RequestBody Variables variables) throws URISyntaxException {
        log.debug("REST request to save Variables : {}", variables);
        if (variables.getId() != null) {
            throw new BadRequestAlertException("A new variables cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Variables result = variablesService.save(variables);
        return ResponseEntity
            .created(new URI("/api/variables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /variables/:id} : Updates an existing variables.
     *
     * @param id the id of the variables to save.
     * @param variables the variables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variables,
     * or with status {@code 400 (Bad Request)} if the variables is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/variables/{id}")
    public ResponseEntity<Variables> updateVariables(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Variables variables
    ) throws URISyntaxException {
        log.debug("REST request to update Variables : {}, {}", id, variables);
        if (variables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!variablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Variables result = variablesService.update(variables);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variables.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /variables/:id} : Partial updates given fields of an existing variables, field will ignore if it is null
     *
     * @param id the id of the variables to save.
     * @param variables the variables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variables,
     * or with status {@code 400 (Bad Request)} if the variables is not valid,
     * or with status {@code 404 (Not Found)} if the variables is not found,
     * or with status {@code 500 (Internal Server Error)} if the variables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/variables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Variables> partialUpdateVariables(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Variables variables
    ) throws URISyntaxException {
        log.debug("REST request to partial update Variables partially : {}, {}", id, variables);
        if (variables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!variablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Variables> result = variablesService.partialUpdate(variables);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variables.getId().toString())
        );
    }

    /**
     * {@code GET  /variables} : get all the variables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variables in body.
     */
    @GetMapping("/variables")
    public ResponseEntity<List<Variables>> getAllVariables() {
        log.debug("REST request to get a page of Variables");
        List<Variables> page = variablesService.findAll();
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /variables/:id} : get the "id" variables.
     *
     * @param id the id of the variables to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variables, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/variables/{id}")
    public ResponseEntity<Variables> getVariables(@PathVariable Long id) {
        log.debug("REST request to get Variables : {}", id);
        Optional<Variables> variables = variablesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(variables);
    }

    /**
     * {@code DELETE  /variables/:id} : delete the "id" variables.
     *
     * @param id the id of the variables to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/variables/{id}")
    public ResponseEntity<Void> deleteVariables(@PathVariable Long id) {
        log.debug("REST request to delete Variables : {}", id);
        variablesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
