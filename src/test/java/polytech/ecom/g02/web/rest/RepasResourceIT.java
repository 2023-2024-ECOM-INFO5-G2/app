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
import polytech.ecom.g02.domain.Repas;
import polytech.ecom.g02.repository.RepasRepository;

/**
 * Integration tests for the {@link RepasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepasResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_APPORT_CALORIQUE = 1F;
    private static final Float UPDATED_APPORT_CALORIQUE = 2F;

    private static final Float DEFAULT_POIDS_CONSOMME = 1F;
    private static final Float UPDATED_POIDS_CONSOMME = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/repas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepasRepository repasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepasMockMvc;

    private Repas repas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repas createEntity(EntityManager em) {
        Repas repas = new Repas()
            .nom(DEFAULT_NOM)
            .date(DEFAULT_DATE)
            .apportCalorique(DEFAULT_APPORT_CALORIQUE)
            .poidsConsomme(DEFAULT_POIDS_CONSOMME)
            .description(DEFAULT_DESCRIPTION);
        return repas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repas createUpdatedEntity(EntityManager em) {
        Repas repas = new Repas()
            .nom(UPDATED_NOM)
            .date(UPDATED_DATE)
            .apportCalorique(UPDATED_APPORT_CALORIQUE)
            .poidsConsomme(UPDATED_POIDS_CONSOMME)
            .description(UPDATED_DESCRIPTION);
        return repas;
    }

    @BeforeEach
    public void initTest() {
        repas = createEntity(em);
    }

    @Test
    @Transactional
    void createRepas() throws Exception {
        int databaseSizeBeforeCreate = repasRepository.findAll().size();
        // Create the Repas
        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isCreated());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeCreate + 1);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRepas.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRepas.getApportCalorique()).isEqualTo(DEFAULT_APPORT_CALORIQUE);
        assertThat(testRepas.getPoidsConsomme()).isEqualTo(DEFAULT_POIDS_CONSOMME);
        assertThat(testRepas.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRepasWithExistingId() throws Exception {
        // Create the Repas with an existing ID
        repas.setId(1L);

        int databaseSizeBeforeCreate = repasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = repasRepository.findAll().size();
        // set the field null
        repas.setNom(null);

        // Create the Repas, which fails.

        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isBadRequest());

        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = repasRepository.findAll().size();
        // set the field null
        repas.setDate(null);

        // Create the Repas, which fails.

        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isBadRequest());

        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        // Get all the repasList
        restRepasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repas.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].apportCalorique").value(hasItem(DEFAULT_APPORT_CALORIQUE.doubleValue())))
            .andExpect(jsonPath("$.[*].poidsConsomme").value(hasItem(DEFAULT_POIDS_CONSOMME.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        // Get the repas
        restRepasMockMvc
            .perform(get(ENTITY_API_URL_ID, repas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repas.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.apportCalorique").value(DEFAULT_APPORT_CALORIQUE.doubleValue()))
            .andExpect(jsonPath("$.poidsConsomme").value(DEFAULT_POIDS_CONSOMME.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRepas() throws Exception {
        // Get the repas
        restRepasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas
        Repas updatedRepas = repasRepository.findById(repas.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRepas are not directly saved in db
        em.detach(updatedRepas);
        updatedRepas
            .nom(UPDATED_NOM)
            .date(UPDATED_DATE)
            .apportCalorique(UPDATED_APPORT_CALORIQUE)
            .poidsConsomme(UPDATED_POIDS_CONSOMME)
            .description(UPDATED_DESCRIPTION);

        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRepas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRepas.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRepas.getApportCalorique()).isEqualTo(UPDATED_APPORT_CALORIQUE);
        assertThat(testRepas.getPoidsConsomme()).isEqualTo(UPDATED_POIDS_CONSOMME);
        assertThat(testRepas.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepasWithPatch() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas using partial update
        Repas partialUpdatedRepas = new Repas();
        partialUpdatedRepas.setId(repas.getId());

        partialUpdatedRepas
            .apportCalorique(UPDATED_APPORT_CALORIQUE)
            .poidsConsomme(UPDATED_POIDS_CONSOMME)
            .description(UPDATED_DESCRIPTION);

        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRepas.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRepas.getApportCalorique()).isEqualTo(UPDATED_APPORT_CALORIQUE);
        assertThat(testRepas.getPoidsConsomme()).isEqualTo(UPDATED_POIDS_CONSOMME);
        assertThat(testRepas.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRepasWithPatch() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas using partial update
        Repas partialUpdatedRepas = new Repas();
        partialUpdatedRepas.setId(repas.getId());

        partialUpdatedRepas
            .nom(UPDATED_NOM)
            .date(UPDATED_DATE)
            .apportCalorique(UPDATED_APPORT_CALORIQUE)
            .poidsConsomme(UPDATED_POIDS_CONSOMME)
            .description(UPDATED_DESCRIPTION);

        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRepas.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRepas.getApportCalorique()).isEqualTo(UPDATED_APPORT_CALORIQUE);
        assertThat(testRepas.getPoidsConsomme()).isEqualTo(UPDATED_POIDS_CONSOMME);
        assertThat(testRepas.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeDelete = repasRepository.findAll().size();

        // Delete the repas
        restRepasMockMvc
            .perform(delete(ENTITY_API_URL_ID, repas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
