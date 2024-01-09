package polytech.g02.ecom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import polytech.g02.ecom.IntegrationTest;
import polytech.g02.ecom.domain.Mesure;
import polytech.g02.ecom.repository.MesureRepository;

/**
 * Integration tests for the {@link MesureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MesureResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOM_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_VALEUR = "BBBBBBBBBB";

    private static final Float DEFAULT_VALEUR = 1F;
    private static final Float UPDATED_VALEUR = 2F;

    private static final String ENTITY_API_URL = "/api/mesures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MesureRepository mesureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMesureMockMvc;

    private Mesure mesure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mesure createEntity(EntityManager em) {
        Mesure mesure = new Mesure().date(DEFAULT_DATE).nomValeur(DEFAULT_NOM_VALEUR).valeur(DEFAULT_VALEUR);
        return mesure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mesure createUpdatedEntity(EntityManager em) {
        Mesure mesure = new Mesure().date(UPDATED_DATE).nomValeur(UPDATED_NOM_VALEUR).valeur(UPDATED_VALEUR);
        return mesure;
    }

    @BeforeEach
    public void initTest() {
        mesure = createEntity(em);
    }

    @Test
    @Transactional
    void createMesure() throws Exception {
        int databaseSizeBeforeCreate = mesureRepository.findAll().size();
        // Create the Mesure
        restMesureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isCreated());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeCreate + 1);
        Mesure testMesure = mesureList.get(mesureList.size() - 1);
        assertThat(testMesure.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMesure.getNomValeur()).isEqualTo(DEFAULT_NOM_VALEUR);
        assertThat(testMesure.getValeur()).isEqualTo(DEFAULT_VALEUR);
    }

    @Test
    @Transactional
    void createMesureWithExistingId() throws Exception {
        // Create the Mesure with an existing ID
        mesure.setId(1L);

        int databaseSizeBeforeCreate = mesureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isBadRequest());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureRepository.findAll().size();
        // set the field null
        mesure.setDate(null);

        // Create the Mesure, which fails.

        restMesureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isBadRequest());

        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureRepository.findAll().size();
        // set the field null
        mesure.setNomValeur(null);

        // Create the Mesure, which fails.

        restMesureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isBadRequest());

        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureRepository.findAll().size();
        // set the field null
        mesure.setValeur(null);

        // Create the Mesure, which fails.

        restMesureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isBadRequest());

        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMesures() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        // Get all the mesureList
        restMesureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesure.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].nomValeur").value(hasItem(DEFAULT_NOM_VALEUR)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())));
    }

    @Test
    @Transactional
    void getMesure() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        // Get the mesure
        restMesureMockMvc
            .perform(get(ENTITY_API_URL_ID, mesure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mesure.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.nomValeur").value(DEFAULT_NOM_VALEUR))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMesure() throws Exception {
        // Get the mesure
        restMesureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMesure() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();

        // Update the mesure
        Mesure updatedMesure = mesureRepository.findById(mesure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMesure are not directly saved in db
        em.detach(updatedMesure);
        updatedMesure.date(UPDATED_DATE).nomValeur(UPDATED_NOM_VALEUR).valeur(UPDATED_VALEUR);

        restMesureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMesure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMesure))
            )
            .andExpect(status().isOk());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
        Mesure testMesure = mesureList.get(mesureList.size() - 1);
        assertThat(testMesure.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMesure.getNomValeur()).isEqualTo(UPDATED_NOM_VALEUR);
        assertThat(testMesure.getValeur()).isEqualTo(UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void putNonExistingMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mesure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMesureWithPatch() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();

        // Update the mesure using partial update
        Mesure partialUpdatedMesure = new Mesure();
        partialUpdatedMesure.setId(mesure.getId());

        partialUpdatedMesure.date(UPDATED_DATE).valeur(UPDATED_VALEUR);

        restMesureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesure))
            )
            .andExpect(status().isOk());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
        Mesure testMesure = mesureList.get(mesureList.size() - 1);
        assertThat(testMesure.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMesure.getNomValeur()).isEqualTo(DEFAULT_NOM_VALEUR);
        assertThat(testMesure.getValeur()).isEqualTo(UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void fullUpdateMesureWithPatch() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();

        // Update the mesure using partial update
        Mesure partialUpdatedMesure = new Mesure();
        partialUpdatedMesure.setId(mesure.getId());

        partialUpdatedMesure.date(UPDATED_DATE).nomValeur(UPDATED_NOM_VALEUR).valeur(UPDATED_VALEUR);

        restMesureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesure))
            )
            .andExpect(status().isOk());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
        Mesure testMesure = mesureList.get(mesureList.size() - 1);
        assertThat(testMesure.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMesure.getNomValeur()).isEqualTo(UPDATED_NOM_VALEUR);
        assertThat(testMesure.getValeur()).isEqualTo(UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void patchNonExistingMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mesure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMesure() throws Exception {
        int databaseSizeBeforeUpdate = mesureRepository.findAll().size();
        mesure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mesure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mesure in the database
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMesure() throws Exception {
        // Initialize the database
        mesureRepository.saveAndFlush(mesure);

        int databaseSizeBeforeDelete = mesureRepository.findAll().size();

        // Delete the mesure
        restMesureMockMvc
            .perform(delete(ENTITY_API_URL_ID, mesure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mesure> mesureList = mesureRepository.findAll();
        assertThat(mesureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
