package com.work.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.work.app.IntegrationTest;
import com.work.app.domain.Campos;
import com.work.app.repository.CamposRepository;
import com.work.app.service.dto.CamposDTO;
import com.work.app.service.mapper.CamposMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CamposResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CamposResourceIT {

    private static final String DEFAULT_NOMBRE_CAMPO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CAMPO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/campos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CamposRepository camposRepository;

    @Autowired
    private CamposMapper camposMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCamposMockMvc;

    private Campos campos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campos createEntity(EntityManager em) {
        Campos campos = new Campos().nombreCampo(DEFAULT_NOMBRE_CAMPO);
        return campos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campos createUpdatedEntity(EntityManager em) {
        Campos campos = new Campos().nombreCampo(UPDATED_NOMBRE_CAMPO);
        return campos;
    }

    @BeforeEach
    public void initTest() {
        campos = createEntity(em);
    }

    @Test
    @Transactional
    void createCampos() throws Exception {
        int databaseSizeBeforeCreate = camposRepository.findAll().size();
        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);
        restCamposMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camposDTO)))
            .andExpect(status().isCreated());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeCreate + 1);
        Campos testCampos = camposList.get(camposList.size() - 1);
        assertThat(testCampos.getNombreCampo()).isEqualTo(DEFAULT_NOMBRE_CAMPO);
    }

    @Test
    @Transactional
    void createCamposWithExistingId() throws Exception {
        // Create the Campos with an existing ID
        campos.setId(1L);
        CamposDTO camposDTO = camposMapper.toDto(campos);

        int databaseSizeBeforeCreate = camposRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCamposMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camposDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCampos() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        // Get all the camposList
        restCamposMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCampo").value(hasItem(DEFAULT_NOMBRE_CAMPO)));
    }

    @Test
    @Transactional
    void getCampos() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        // Get the campos
        restCamposMockMvc
            .perform(get(ENTITY_API_URL_ID, campos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campos.getId().intValue()))
            .andExpect(jsonPath("$.nombreCampo").value(DEFAULT_NOMBRE_CAMPO));
    }

    @Test
    @Transactional
    void getNonExistingCampos() throws Exception {
        // Get the campos
        restCamposMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCampos() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        int databaseSizeBeforeUpdate = camposRepository.findAll().size();

        // Update the campos
        Campos updatedCampos = camposRepository.findById(campos.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCampos are not directly saved in db
        em.detach(updatedCampos);
        updatedCampos.nombreCampo(UPDATED_NOMBRE_CAMPO);
        CamposDTO camposDTO = camposMapper.toDto(updatedCampos);

        restCamposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, camposDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isOk());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
        Campos testCampos = camposList.get(camposList.size() - 1);
        assertThat(testCampos.getNombreCampo()).isEqualTo(UPDATED_NOMBRE_CAMPO);
    }

    @Test
    @Transactional
    void putNonExistingCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, camposDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camposDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCamposWithPatch() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        int databaseSizeBeforeUpdate = camposRepository.findAll().size();

        // Update the campos using partial update
        Campos partialUpdatedCampos = new Campos();
        partialUpdatedCampos.setId(campos.getId());

        partialUpdatedCampos.nombreCampo(UPDATED_NOMBRE_CAMPO);

        restCamposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampos))
            )
            .andExpect(status().isOk());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
        Campos testCampos = camposList.get(camposList.size() - 1);
        assertThat(testCampos.getNombreCampo()).isEqualTo(UPDATED_NOMBRE_CAMPO);
    }

    @Test
    @Transactional
    void fullUpdateCamposWithPatch() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        int databaseSizeBeforeUpdate = camposRepository.findAll().size();

        // Update the campos using partial update
        Campos partialUpdatedCampos = new Campos();
        partialUpdatedCampos.setId(campos.getId());

        partialUpdatedCampos.nombreCampo(UPDATED_NOMBRE_CAMPO);

        restCamposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampos))
            )
            .andExpect(status().isOk());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
        Campos testCampos = camposList.get(camposList.size() - 1);
        assertThat(testCampos.getNombreCampo()).isEqualTo(UPDATED_NOMBRE_CAMPO);
    }

    @Test
    @Transactional
    void patchNonExistingCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, camposDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampos() throws Exception {
        int databaseSizeBeforeUpdate = camposRepository.findAll().size();
        campos.setId(count.incrementAndGet());

        // Create the Campos
        CamposDTO camposDTO = camposMapper.toDto(campos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamposMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(camposDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campos in the database
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCampos() throws Exception {
        // Initialize the database
        camposRepository.saveAndFlush(campos);

        int databaseSizeBeforeDelete = camposRepository.findAll().size();

        // Delete the campos
        restCamposMockMvc
            .perform(delete(ENTITY_API_URL_ID, campos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campos> camposList = camposRepository.findAll();
        assertThat(camposList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
