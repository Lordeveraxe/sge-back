package com.work.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.work.app.IntegrationTest;
import com.work.app.domain.Contadores;
import com.work.app.domain.enumeration.Estado;
import com.work.app.domain.enumeration.TipoContador;
import com.work.app.repository.ContadoresRepository;
import com.work.app.service.dto.ContadoresDTO;
import com.work.app.service.mapper.ContadoresMapper;
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
 * Integration tests for the {@link ContadoresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContadoresResourceIT {

    private static final TipoContador DEFAULT_TIPO_CONTADOR = TipoContador.GENERADORES;
    private static final TipoContador UPDATED_TIPO_CONTADOR = TipoContador.CONTADORES;

    private static final Instant DEFAULT_FECHA_INGRESO_CONTADOR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INGRESO_CONTADOR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.ACTIVO;
    private static final Estado UPDATED_ESTADO = Estado.INACTIVO;

    private static final String ENTITY_API_URL = "/api/contadores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContadoresRepository contadoresRepository;

    @Autowired
    private ContadoresMapper contadoresMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContadoresMockMvc;

    private Contadores contadores;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contadores createEntity(EntityManager em) {
        Contadores contadores = new Contadores()
            .tipoContador(DEFAULT_TIPO_CONTADOR)
            .fechaIngresoContador(DEFAULT_FECHA_INGRESO_CONTADOR)
            .tag(DEFAULT_TAG)
            .estado(DEFAULT_ESTADO);
        return contadores;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contadores createUpdatedEntity(EntityManager em) {
        Contadores contadores = new Contadores()
            .tipoContador(UPDATED_TIPO_CONTADOR)
            .fechaIngresoContador(UPDATED_FECHA_INGRESO_CONTADOR)
            .tag(UPDATED_TAG)
            .estado(UPDATED_ESTADO);
        return contadores;
    }

    @BeforeEach
    public void initTest() {
        contadores = createEntity(em);
    }

    @Test
    @Transactional
    void createContadores() throws Exception {
        int databaseSizeBeforeCreate = contadoresRepository.findAll().size();
        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);
        restContadoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contadoresDTO)))
            .andExpect(status().isCreated());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeCreate + 1);
        Contadores testContadores = contadoresList.get(contadoresList.size() - 1);
        assertThat(testContadores.getTipoContador()).isEqualTo(DEFAULT_TIPO_CONTADOR);
        assertThat(testContadores.getFechaIngresoContador()).isEqualTo(DEFAULT_FECHA_INGRESO_CONTADOR);
        assertThat(testContadores.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testContadores.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createContadoresWithExistingId() throws Exception {
        // Create the Contadores with an existing ID
        contadores.setId(1L);
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        int databaseSizeBeforeCreate = contadoresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContadoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contadoresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContadores() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        // Get all the contadoresList
        restContadoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contadores.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoContador").value(hasItem(DEFAULT_TIPO_CONTADOR.toString())))
            .andExpect(jsonPath("$.[*].fechaIngresoContador").value(hasItem(DEFAULT_FECHA_INGRESO_CONTADOR.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getContadores() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        // Get the contadores
        restContadoresMockMvc
            .perform(get(ENTITY_API_URL_ID, contadores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contadores.getId().intValue()))
            .andExpect(jsonPath("$.tipoContador").value(DEFAULT_TIPO_CONTADOR.toString()))
            .andExpect(jsonPath("$.fechaIngresoContador").value(DEFAULT_FECHA_INGRESO_CONTADOR.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContadores() throws Exception {
        // Get the contadores
        restContadoresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContadores() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();

        // Update the contadores
        Contadores updatedContadores = contadoresRepository.findById(contadores.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContadores are not directly saved in db
        em.detach(updatedContadores);
        updatedContadores
            .tipoContador(UPDATED_TIPO_CONTADOR)
            .fechaIngresoContador(UPDATED_FECHA_INGRESO_CONTADOR)
            .tag(UPDATED_TAG)
            .estado(UPDATED_ESTADO);
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(updatedContadores);

        restContadoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contadoresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
        Contadores testContadores = contadoresList.get(contadoresList.size() - 1);
        assertThat(testContadores.getTipoContador()).isEqualTo(UPDATED_TIPO_CONTADOR);
        assertThat(testContadores.getFechaIngresoContador()).isEqualTo(UPDATED_FECHA_INGRESO_CONTADOR);
        assertThat(testContadores.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testContadores.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contadoresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contadoresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContadoresWithPatch() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();

        // Update the contadores using partial update
        Contadores partialUpdatedContadores = new Contadores();
        partialUpdatedContadores.setId(contadores.getId());

        partialUpdatedContadores.fechaIngresoContador(UPDATED_FECHA_INGRESO_CONTADOR).tag(UPDATED_TAG);

        restContadoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContadores.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContadores))
            )
            .andExpect(status().isOk());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
        Contadores testContadores = contadoresList.get(contadoresList.size() - 1);
        assertThat(testContadores.getTipoContador()).isEqualTo(DEFAULT_TIPO_CONTADOR);
        assertThat(testContadores.getFechaIngresoContador()).isEqualTo(UPDATED_FECHA_INGRESO_CONTADOR);
        assertThat(testContadores.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testContadores.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateContadoresWithPatch() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();

        // Update the contadores using partial update
        Contadores partialUpdatedContadores = new Contadores();
        partialUpdatedContadores.setId(contadores.getId());

        partialUpdatedContadores
            .tipoContador(UPDATED_TIPO_CONTADOR)
            .fechaIngresoContador(UPDATED_FECHA_INGRESO_CONTADOR)
            .tag(UPDATED_TAG)
            .estado(UPDATED_ESTADO);

        restContadoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContadores.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContadores))
            )
            .andExpect(status().isOk());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
        Contadores testContadores = contadoresList.get(contadoresList.size() - 1);
        assertThat(testContadores.getTipoContador()).isEqualTo(UPDATED_TIPO_CONTADOR);
        assertThat(testContadores.getFechaIngresoContador()).isEqualTo(UPDATED_FECHA_INGRESO_CONTADOR);
        assertThat(testContadores.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testContadores.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contadoresDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContadores() throws Exception {
        int databaseSizeBeforeUpdate = contadoresRepository.findAll().size();
        contadores.setId(count.incrementAndGet());

        // Create the Contadores
        ContadoresDTO contadoresDTO = contadoresMapper.toDto(contadores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadoresMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contadoresDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contadores in the database
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContadores() throws Exception {
        // Initialize the database
        contadoresRepository.saveAndFlush(contadores);

        int databaseSizeBeforeDelete = contadoresRepository.findAll().size();

        // Delete the contadores
        restContadoresMockMvc
            .perform(delete(ENTITY_API_URL_ID, contadores.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contadores> contadoresList = contadoresRepository.findAll();
        assertThat(contadoresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
