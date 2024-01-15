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
import polytech.ecom.g02.domain.MesureEPA;
import polytech.ecom.g02.repository.MesureEPARepository;

/**
 * Integration tests for the {@link MesureEPAResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MesureEPAResourceIT {

    private static final Float DEFAULT_VALEUR = 0F;
    private static final Float UPDATED_VALEUR = 1F;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/mesure-epas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MesureEPARepository mesureEPARepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMesureEPAMockMvc;

    private MesureEPA mesureEPA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesureEPA createEntity(EntityManager em) {
        MesureEPA mesureEPA = new MesureEPA().valeur(DEFAULT_VALEUR).date(DEFAULT_DATE);
        return mesureEPA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesureEPA createUpdatedEntity(EntityManager em) {
        MesureEPA mesureEPA = new MesureEPA().valeur(UPDATED_VALEUR).date(UPDATED_DATE);
        return mesureEPA;
    }

    @BeforeEach
    public void initTest() {
        mesureEPA = createEntity(em);
    }

    @Test
    @Transactional
    void createMesureEPA() throws Exception {
        int databaseSizeBeforeCreate = mesureEPARepository.findAll().size();
        // Create the MesureEPA
        restMesureEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureEPA)))
            .andExpect(status().isCreated());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeCreate + 1);
        MesureEPA testMesureEPA = mesureEPAList.get(mesureEPAList.size() - 1);
        assertThat(testMesureEPA.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesureEPA.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createMesureEPAWithExistingId() throws Exception {
        // Create the MesureEPA with an existing ID
        mesureEPA.setId(1L);

        int databaseSizeBeforeCreate = mesureEPARepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesureEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureEPA)))
            .andExpect(status().isBadRequest());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureEPARepository.findAll().size();
        // set the field null
        mesureEPA.setValeur(null);

        // Create the MesureEPA, which fails.

        restMesureEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureEPA)))
            .andExpect(status().isBadRequest());

        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureEPARepository.findAll().size();
        // set the field null
        mesureEPA.setDate(null);

        // Create the MesureEPA, which fails.

        restMesureEPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureEPA)))
            .andExpect(status().isBadRequest());

        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMesureEPAS() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        // Get all the mesureEPAList
        restMesureEPAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesureEPA.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getMesureEPA() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        // Get the mesureEPA
        restMesureEPAMockMvc
            .perform(get(ENTITY_API_URL_ID, mesureEPA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mesureEPA.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingMesureEPA() throws Exception {
        // Get the mesureEPA
        restMesureEPAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMesureEPA() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();

        // Update the mesureEPA
        MesureEPA updatedMesureEPA = mesureEPARepository.findById(mesureEPA.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMesureEPA are not directly saved in db
        em.detach(updatedMesureEPA);
        updatedMesureEPA.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesureEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMesureEPA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMesureEPA))
            )
            .andExpect(status().isOk());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
        MesureEPA testMesureEPA = mesureEPAList.get(mesureEPAList.size() - 1);
        assertThat(testMesureEPA.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesureEPA.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mesureEPA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesureEPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesureEPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureEPA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMesureEPAWithPatch() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();

        // Update the mesureEPA using partial update
        MesureEPA partialUpdatedMesureEPA = new MesureEPA();
        partialUpdatedMesureEPA.setId(mesureEPA.getId());

        restMesureEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesureEPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesureEPA))
            )
            .andExpect(status().isOk());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
        MesureEPA testMesureEPA = mesureEPAList.get(mesureEPAList.size() - 1);
        assertThat(testMesureEPA.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesureEPA.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMesureEPAWithPatch() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();

        // Update the mesureEPA using partial update
        MesureEPA partialUpdatedMesureEPA = new MesureEPA();
        partialUpdatedMesureEPA.setId(mesureEPA.getId());

        partialUpdatedMesureEPA.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesureEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesureEPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesureEPA))
            )
            .andExpect(status().isOk());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
        MesureEPA testMesureEPA = mesureEPAList.get(mesureEPAList.size() - 1);
        assertThat(testMesureEPA.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesureEPA.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mesureEPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesureEPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesureEPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMesureEPA() throws Exception {
        int databaseSizeBeforeUpdate = mesureEPARepository.findAll().size();
        mesureEPA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureEPAMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mesureEPA))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesureEPA in the database
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMesureEPA() throws Exception {
        // Initialize the database
        mesureEPARepository.saveAndFlush(mesureEPA);

        int databaseSizeBeforeDelete = mesureEPARepository.findAll().size();

        // Delete the mesureEPA
        restMesureEPAMockMvc
            .perform(delete(ENTITY_API_URL_ID, mesureEPA.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MesureEPA> mesureEPAList = mesureEPARepository.findAll();
        assertThat(mesureEPAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
