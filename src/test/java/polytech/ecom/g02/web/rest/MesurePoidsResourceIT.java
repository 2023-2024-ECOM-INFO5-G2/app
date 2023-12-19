package polytech.ecom.g02.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static polytech.ecom.g02.web.rest.TestUtil.sameInstant;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import polytech.ecom.g02.IntegrationTest;
import polytech.ecom.g02.domain.MesurePoids;
import polytech.ecom.g02.repository.MesurePoidsRepository;

/**
 * Integration tests for the {@link MesurePoidsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MesurePoidsResourceIT {

    private static final Float DEFAULT_VALEUR = 2F;
    private static final Float UPDATED_VALEUR = 3F;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/mesure-poids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MesurePoidsRepository mesurePoidsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMesurePoidsMockMvc;

    private MesurePoids mesurePoids;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesurePoids createEntity(EntityManager em) {
        MesurePoids mesurePoids = new MesurePoids().valeur(DEFAULT_VALEUR).date(DEFAULT_DATE);
        return mesurePoids;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesurePoids createUpdatedEntity(EntityManager em) {
        MesurePoids mesurePoids = new MesurePoids().valeur(UPDATED_VALEUR).date(UPDATED_DATE);
        return mesurePoids;
    }

    @BeforeEach
    public void initTest() {
        mesurePoids = createEntity(em);
    }

    @Test
    @Transactional
    void createMesurePoids() throws Exception {
        int databaseSizeBeforeCreate = mesurePoidsRepository.findAll().size();
        // Create the MesurePoids
        restMesurePoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesurePoids)))
            .andExpect(status().isCreated());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeCreate + 1);
        MesurePoids testMesurePoids = mesurePoidsList.get(mesurePoidsList.size() - 1);
        assertThat(testMesurePoids.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesurePoids.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createMesurePoidsWithExistingId() throws Exception {
        // Create the MesurePoids with an existing ID
        mesurePoids.setId(1L);

        int databaseSizeBeforeCreate = mesurePoidsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesurePoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesurePoids)))
            .andExpect(status().isBadRequest());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesurePoidsRepository.findAll().size();
        // set the field null
        mesurePoids.setValeur(null);

        // Create the MesurePoids, which fails.

        restMesurePoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesurePoids)))
            .andExpect(status().isBadRequest());

        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesurePoidsRepository.findAll().size();
        // set the field null
        mesurePoids.setDate(null);

        // Create the MesurePoids, which fails.

        restMesurePoidsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesurePoids)))
            .andExpect(status().isBadRequest());

        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMesurePoids() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        // Get all the mesurePoidsList
        restMesurePoidsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesurePoids.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getMesurePoids() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        // Get the mesurePoids
        restMesurePoidsMockMvc
            .perform(get(ENTITY_API_URL_ID, mesurePoids.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mesurePoids.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingMesurePoids() throws Exception {
        // Get the mesurePoids
        restMesurePoidsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMesurePoids() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();

        // Update the mesurePoids
        MesurePoids updatedMesurePoids = mesurePoidsRepository.findById(mesurePoids.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMesurePoids are not directly saved in db
        em.detach(updatedMesurePoids);
        updatedMesurePoids.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesurePoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMesurePoids.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMesurePoids))
            )
            .andExpect(status().isOk());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
        MesurePoids testMesurePoids = mesurePoidsList.get(mesurePoidsList.size() - 1);
        assertThat(testMesurePoids.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesurePoids.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mesurePoids.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesurePoids))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesurePoids))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesurePoids)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMesurePoidsWithPatch() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();

        // Update the mesurePoids using partial update
        MesurePoids partialUpdatedMesurePoids = new MesurePoids();
        partialUpdatedMesurePoids.setId(mesurePoids.getId());

        partialUpdatedMesurePoids.date(UPDATED_DATE);

        restMesurePoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesurePoids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesurePoids))
            )
            .andExpect(status().isOk());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
        MesurePoids testMesurePoids = mesurePoidsList.get(mesurePoidsList.size() - 1);
        assertThat(testMesurePoids.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesurePoids.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMesurePoidsWithPatch() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();

        // Update the mesurePoids using partial update
        MesurePoids partialUpdatedMesurePoids = new MesurePoids();
        partialUpdatedMesurePoids.setId(mesurePoids.getId());

        partialUpdatedMesurePoids.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesurePoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesurePoids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesurePoids))
            )
            .andExpect(status().isOk());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
        MesurePoids testMesurePoids = mesurePoidsList.get(mesurePoidsList.size() - 1);
        assertThat(testMesurePoids.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesurePoids.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mesurePoids.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesurePoids))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesurePoids))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMesurePoids() throws Exception {
        int databaseSizeBeforeUpdate = mesurePoidsRepository.findAll().size();
        mesurePoids.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesurePoidsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mesurePoids))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesurePoids in the database
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMesurePoids() throws Exception {
        // Initialize the database
        mesurePoidsRepository.saveAndFlush(mesurePoids);

        int databaseSizeBeforeDelete = mesurePoidsRepository.findAll().size();

        // Delete the mesurePoids
        restMesurePoidsMockMvc
            .perform(delete(ENTITY_API_URL_ID, mesurePoids.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MesurePoids> mesurePoidsList = mesurePoidsRepository.findAll();
        assertThat(mesurePoidsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
