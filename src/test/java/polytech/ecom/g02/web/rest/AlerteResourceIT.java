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
import polytech.ecom.g02.domain.Alerte;
import polytech.ecom.g02.repository.AlerteRepository;

/**
 * Integration tests for the {@link AlerteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_SEVERE = false;
    private static final Boolean UPDATED_SEVERE = true;

    private static final String ENTITY_API_URL = "/api/alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteMockMvc;

    private Alerte alerte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createEntity(EntityManager em) {
        Alerte alerte = new Alerte().description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE).severe(DEFAULT_SEVERE);
        return alerte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createUpdatedEntity(EntityManager em) {
        Alerte alerte = new Alerte().description(UPDATED_DESCRIPTION).date(UPDATED_DATE).severe(UPDATED_SEVERE);
        return alerte;
    }

    @BeforeEach
    public void initTest() {
        alerte = createEntity(em);
    }

    @Test
    @Transactional
    void createAlerte() throws Exception {
        int databaseSizeBeforeCreate = alerteRepository.findAll().size();
        // Create the Alerte
        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isCreated());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeCreate + 1);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlerte.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAlerte.getSevere()).isEqualTo(DEFAULT_SEVERE);
    }

    @Test
    @Transactional
    void createAlerteWithExistingId() throws Exception {
        // Create the Alerte with an existing ID
        alerte.setId(1L);

        int databaseSizeBeforeCreate = alerteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = alerteRepository.findAll().size();
        // set the field null
        alerte.setDescription(null);

        // Create the Alerte, which fails.

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isBadRequest());

        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = alerteRepository.findAll().size();
        // set the field null
        alerte.setDate(null);

        // Create the Alerte, which fails.

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isBadRequest());

        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSevereIsRequired() throws Exception {
        int databaseSizeBeforeTest = alerteRepository.findAll().size();
        // set the field null
        alerte.setSevere(null);

        // Create the Alerte, which fails.

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isBadRequest());

        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlertes() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].severe").value(hasItem(DEFAULT_SEVERE.booleanValue())));
    }

    @Test
    @Transactional
    void getAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        // Get the alerte
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL_ID, alerte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerte.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.severe").value(DEFAULT_SEVERE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAlerte() throws Exception {
        // Get the alerte
        restAlerteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();

        // Update the alerte
        Alerte updatedAlerte = alerteRepository.findById(alerte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerte are not directly saved in db
        em.detach(updatedAlerte);
        updatedAlerte.description(UPDATED_DESCRIPTION).date(UPDATED_DATE).severe(UPDATED_SEVERE);

        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlerte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlerte.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAlerte.getSevere()).isEqualTo(UPDATED_SEVERE);
    }

    @Test
    @Transactional
    void putNonExistingAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.description(UPDATED_DESCRIPTION);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlerte.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAlerte.getSevere()).isEqualTo(DEFAULT_SEVERE);
    }

    @Test
    @Transactional
    void fullUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.description(UPDATED_DESCRIPTION).date(UPDATED_DATE).severe(UPDATED_SEVERE);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
        Alerte testAlerte = alerteList.get(alerteList.size() - 1);
        assertThat(testAlerte.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlerte.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAlerte.getSevere()).isEqualTo(UPDATED_SEVERE);
    }

    @Test
    @Transactional
    void patchNonExistingAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerte() throws Exception {
        int databaseSizeBeforeUpdate = alerteRepository.findAll().size();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alerte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        int databaseSizeBeforeDelete = alerteRepository.findAll().size();

        // Delete the alerte
        restAlerteMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alerte> alerteList = alerteRepository.findAll();
        assertThat(alerteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
