package com.work.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.work.app.IntegrationTest;
import com.work.app.domain.Variables;
import com.work.app.repository.VariablesRepository;
import com.work.app.service.dto.VariablesDTO;
import com.work.app.service.mapper.VariablesMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VariablesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VariablesResourceIT {

    private static final String DEFAULT_VARIABLES = "AAAAAAAAAA";
    private static final String UPDATED_VARIABLES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VariablesRepository variablesRepository;

    @Autowired
    private VariablesMapper variablesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVariablesMockMvc;

    private Variables variables;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variables createEntity(EntityManager em) {
        Variables variables = new Variables().variables(DEFAULT_VARIABLES);
        return variables;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variables createUpdatedEntity(EntityManager em) {
        Variables variables = new Variables().variables(UPDATED_VARIABLES);
        return variables;
    }

    @BeforeEach
    public void initTest() {
        variables = createEntity(em);
    }

    @Test
    @Transactional
    void createVariables() throws Exception {
        int databaseSizeBeforeCreate = variablesRepository.findAll().size();
        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);
        restVariablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variablesDTO)))
            .andExpect(status().isCreated());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeCreate + 1);
        Variables testVariables = variablesList.get(variablesList.size() - 1);
        assertThat(testVariables.getVariables()).isEqualTo(DEFAULT_VARIABLES);
    }

    @Test
    @Transactional
    void createVariablesWithExistingId() throws Exception {
        // Create the Variables with an existing ID
        variables.setId(1L);
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        int databaseSizeBeforeCreate = variablesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVariablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variablesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        // Get all the variablesList
        restVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variables.getId().intValue())))
            .andExpect(jsonPath("$.[*].variables").value(hasItem(DEFAULT_VARIABLES)));
    }

    @Test
    @Transactional
    void getVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        // Get the variables
        restVariablesMockMvc
            .perform(get(ENTITY_API_URL_ID, variables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(variables.getId().intValue()))
            .andExpect(jsonPath("$.variables").value(DEFAULT_VARIABLES));
    }

    @Test
    @Transactional
    void getNonExistingVariables() throws Exception {
        // Get the variables
        restVariablesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();

        // Update the variables
        Variables updatedVariables = variablesRepository.findById(variables.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVariables are not directly saved in db
        em.detach(updatedVariables);
        updatedVariables.variables(UPDATED_VARIABLES);
        VariablesDTO variablesDTO = variablesMapper.toDto(updatedVariables);

        restVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, variablesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
        Variables testVariables = variablesList.get(variablesList.size() - 1);
        assertThat(testVariables.getVariables()).isEqualTo(UPDATED_VARIABLES);
    }

    @Test
    @Transactional
    void putNonExistingVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, variablesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variablesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVariablesWithPatch() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();

        // Update the variables using partial update
        Variables partialUpdatedVariables = new Variables();
        partialUpdatedVariables.setId(variables.getId());

        partialUpdatedVariables.variables(UPDATED_VARIABLES);

        restVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariables))
            )
            .andExpect(status().isOk());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
        Variables testVariables = variablesList.get(variablesList.size() - 1);
        assertThat(testVariables.getVariables()).isEqualTo(UPDATED_VARIABLES);
    }

    @Test
    @Transactional
    void fullUpdateVariablesWithPatch() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();

        // Update the variables using partial update
        Variables partialUpdatedVariables = new Variables();
        partialUpdatedVariables.setId(variables.getId());

        partialUpdatedVariables.variables(UPDATED_VARIABLES);

        restVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariables))
            )
            .andExpect(status().isOk());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
        Variables testVariables = variablesList.get(variablesList.size() - 1);
        assertThat(testVariables.getVariables()).isEqualTo(UPDATED_VARIABLES);
    }

    @Test
    @Transactional
    void patchNonExistingVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, variablesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVariables() throws Exception {
        int databaseSizeBeforeUpdate = variablesRepository.findAll().size();
        variables.setId(count.incrementAndGet());

        // Create the Variables
        VariablesDTO variablesDTO = variablesMapper.toDto(variables);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(variablesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variables in the database
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVariables() throws Exception {
        // Initialize the database
        variablesRepository.saveAndFlush(variables);

        int databaseSizeBeforeDelete = variablesRepository.findAll().size();

        // Delete the variables
        restVariablesMockMvc
            .perform(delete(ENTITY_API_URL_ID, variables.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Variables> variablesList = variablesRepository.findAll();
        assertThat(variablesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
