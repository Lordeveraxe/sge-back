package com.work.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.work.app.IntegrationTest;
import com.work.app.domain.Registros;
import com.work.app.repository.RegistrosRepository;
import com.work.app.service.dto.RegistrosDTO;
import com.work.app.service.mapper.RegistrosMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link RegistrosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegistrosResourceIT {

    private static final Instant DEFAULT_FECHA_REGISTRO_VARIABLE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO_VARIABLE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REGISTRO_VARIABLE_CONTADOR = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRO_VARIABLE_CONTADOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/registros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistrosRepository registrosRepository;

    @Autowired
    private RegistrosMapper registrosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistrosMockMvc;

    private Registros registros;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registros createEntity(EntityManager em) {
        Registros registros = new Registros()
            .fechaRegistroVariable(DEFAULT_FECHA_REGISTRO_VARIABLE)
            .registroVariableContador(DEFAULT_REGISTRO_VARIABLE_CONTADOR);
        return registros;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registros createUpdatedEntity(EntityManager em) {
        Registros registros = new Registros()
            .fechaRegistroVariable(UPDATED_FECHA_REGISTRO_VARIABLE)
            .registroVariableContador(UPDATED_REGISTRO_VARIABLE_CONTADOR);
        return registros;
    }

    @BeforeEach
    public void initTest() {
        registros = createEntity(em);
    }

    @Test
    @Transactional
    void createRegistros() throws Exception {
        int databaseSizeBeforeCreate = registrosRepository.findAll().size();
        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);
        restRegistrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registrosDTO)))
            .andExpect(status().isCreated());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeCreate + 1);
        Registros testRegistros = registrosList.get(registrosList.size() - 1);
        assertThat(testRegistros.getFechaRegistroVariable()).isEqualTo(DEFAULT_FECHA_REGISTRO_VARIABLE);
        assertThat(testRegistros.getRegistroVariableContador()).isEqualTo(DEFAULT_REGISTRO_VARIABLE_CONTADOR);
    }

    @Test
    @Transactional
    void createRegistrosWithExistingId() throws Exception {
        // Create the Registros with an existing ID
        registros.setId(1L);
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        int databaseSizeBeforeCreate = registrosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registrosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegistros() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        // Get all the registrosList
        restRegistrosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registros.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaRegistroVariable").value(hasItem(DEFAULT_FECHA_REGISTRO_VARIABLE.toString())))
            .andExpect(jsonPath("$.[*].registroVariableContador").value(hasItem(DEFAULT_REGISTRO_VARIABLE_CONTADOR)));
    }

    @Test
    @Transactional
    void getRegistros() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        // Get the registros
        restRegistrosMockMvc
            .perform(get(ENTITY_API_URL_ID, registros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registros.getId().intValue()))
            .andExpect(jsonPath("$.fechaRegistroVariable").value(DEFAULT_FECHA_REGISTRO_VARIABLE.toString()))
            .andExpect(jsonPath("$.registroVariableContador").value(DEFAULT_REGISTRO_VARIABLE_CONTADOR));
    }

    @Test
    @Transactional
    void getNonExistingRegistros() throws Exception {
        // Get the registros
        restRegistrosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegistros() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();

        // Update the registros
        Registros updatedRegistros = registrosRepository.findById(registros.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRegistros are not directly saved in db
        em.detach(updatedRegistros);
        updatedRegistros
            .fechaRegistroVariable(UPDATED_FECHA_REGISTRO_VARIABLE)
            .registroVariableContador(UPDATED_REGISTRO_VARIABLE_CONTADOR);
        RegistrosDTO registrosDTO = registrosMapper.toDto(updatedRegistros);

        restRegistrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registrosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
        Registros testRegistros = registrosList.get(registrosList.size() - 1);
        assertThat(testRegistros.getFechaRegistroVariable()).isEqualTo(UPDATED_FECHA_REGISTRO_VARIABLE);
        assertThat(testRegistros.getRegistroVariableContador()).isEqualTo(UPDATED_REGISTRO_VARIABLE_CONTADOR);
    }

    @Test
    @Transactional
    void putNonExistingRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registrosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registrosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistrosWithPatch() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();

        // Update the registros using partial update
        Registros partialUpdatedRegistros = new Registros();
        partialUpdatedRegistros.setId(registros.getId());

        partialUpdatedRegistros
            .fechaRegistroVariable(UPDATED_FECHA_REGISTRO_VARIABLE)
            .registroVariableContador(UPDATED_REGISTRO_VARIABLE_CONTADOR);

        restRegistrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistros.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistros))
            )
            .andExpect(status().isOk());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
        Registros testRegistros = registrosList.get(registrosList.size() - 1);
        assertThat(testRegistros.getFechaRegistroVariable()).isEqualTo(UPDATED_FECHA_REGISTRO_VARIABLE);
        assertThat(testRegistros.getRegistroVariableContador()).isEqualTo(UPDATED_REGISTRO_VARIABLE_CONTADOR);
    }

    @Test
    @Transactional
    void fullUpdateRegistrosWithPatch() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();

        // Update the registros using partial update
        Registros partialUpdatedRegistros = new Registros();
        partialUpdatedRegistros.setId(registros.getId());

        partialUpdatedRegistros
            .fechaRegistroVariable(UPDATED_FECHA_REGISTRO_VARIABLE)
            .registroVariableContador(UPDATED_REGISTRO_VARIABLE_CONTADOR);

        restRegistrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistros.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistros))
            )
            .andExpect(status().isOk());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
        Registros testRegistros = registrosList.get(registrosList.size() - 1);
        assertThat(testRegistros.getFechaRegistroVariable()).isEqualTo(UPDATED_FECHA_REGISTRO_VARIABLE);
        assertThat(testRegistros.getRegistroVariableContador()).isEqualTo(UPDATED_REGISTRO_VARIABLE_CONTADOR);
    }

    @Test
    @Transactional
    void patchNonExistingRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registrosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistros() throws Exception {
        int databaseSizeBeforeUpdate = registrosRepository.findAll().size();
        registros.setId(count.incrementAndGet());

        // Create the Registros
        RegistrosDTO registrosDTO = registrosMapper.toDto(registros);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistrosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(registrosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registros in the database
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistros() throws Exception {
        // Initialize the database
        registrosRepository.saveAndFlush(registros);

        int databaseSizeBeforeDelete = registrosRepository.findAll().size();

        // Delete the registros
        restRegistrosMockMvc
            .perform(delete(ENTITY_API_URL_ID, registros.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Registros> registrosList = registrosRepository.findAll();
        assertThat(registrosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
