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
import polytech.ecom.g02.domain.MesureAlbumine;
import polytech.ecom.g02.repository.MesureAlbumineRepository;

/**
 * Integration tests for the {@link MesureAlbumineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MesureAlbumineResourceIT {

    private static final Float DEFAULT_VALEUR = 0F;
    private static final Float UPDATED_VALEUR = 1F;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/mesure-albumines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MesureAlbumineRepository mesureAlbumineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMesureAlbumineMockMvc;

    private MesureAlbumine mesureAlbumine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesureAlbumine createEntity(EntityManager em) {
        MesureAlbumine mesureAlbumine = new MesureAlbumine().valeur(DEFAULT_VALEUR).date(DEFAULT_DATE);
        return mesureAlbumine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MesureAlbumine createUpdatedEntity(EntityManager em) {
        MesureAlbumine mesureAlbumine = new MesureAlbumine().valeur(UPDATED_VALEUR).date(UPDATED_DATE);
        return mesureAlbumine;
    }

    @BeforeEach
    public void initTest() {
        mesureAlbumine = createEntity(em);
    }

    @Test
    @Transactional
    void createMesureAlbumine() throws Exception {
        int databaseSizeBeforeCreate = mesureAlbumineRepository.findAll().size();
        // Create the MesureAlbumine
        restMesureAlbumineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isCreated());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeCreate + 1);
        MesureAlbumine testMesureAlbumine = mesureAlbumineList.get(mesureAlbumineList.size() - 1);
        assertThat(testMesureAlbumine.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesureAlbumine.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createMesureAlbumineWithExistingId() throws Exception {
        // Create the MesureAlbumine with an existing ID
        mesureAlbumine.setId(1L);

        int databaseSizeBeforeCreate = mesureAlbumineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesureAlbumineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureAlbumineRepository.findAll().size();
        // set the field null
        mesureAlbumine.setValeur(null);

        // Create the MesureAlbumine, which fails.

        restMesureAlbumineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesureAlbumineRepository.findAll().size();
        // set the field null
        mesureAlbumine.setDate(null);

        // Create the MesureAlbumine, which fails.

        restMesureAlbumineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMesureAlbumines() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        // Get all the mesureAlbumineList
        restMesureAlbumineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesureAlbumine.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getMesureAlbumine() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        // Get the mesureAlbumine
        restMesureAlbumineMockMvc
            .perform(get(ENTITY_API_URL_ID, mesureAlbumine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mesureAlbumine.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingMesureAlbumine() throws Exception {
        // Get the mesureAlbumine
        restMesureAlbumineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMesureAlbumine() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();

        // Update the mesureAlbumine
        MesureAlbumine updatedMesureAlbumine = mesureAlbumineRepository.findById(mesureAlbumine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMesureAlbumine are not directly saved in db
        em.detach(updatedMesureAlbumine);
        updatedMesureAlbumine.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesureAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMesureAlbumine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMesureAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
        MesureAlbumine testMesureAlbumine = mesureAlbumineList.get(mesureAlbumineList.size() - 1);
        assertThat(testMesureAlbumine.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesureAlbumine.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mesureAlbumine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesureAlbumine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMesureAlbumineWithPatch() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();

        // Update the mesureAlbumine using partial update
        MesureAlbumine partialUpdatedMesureAlbumine = new MesureAlbumine();
        partialUpdatedMesureAlbumine.setId(mesureAlbumine.getId());

        partialUpdatedMesureAlbumine.date(UPDATED_DATE);

        restMesureAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesureAlbumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesureAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
        MesureAlbumine testMesureAlbumine = mesureAlbumineList.get(mesureAlbumineList.size() - 1);
        assertThat(testMesureAlbumine.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testMesureAlbumine.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMesureAlbumineWithPatch() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();

        // Update the mesureAlbumine using partial update
        MesureAlbumine partialUpdatedMesureAlbumine = new MesureAlbumine();
        partialUpdatedMesureAlbumine.setId(mesureAlbumine.getId());

        partialUpdatedMesureAlbumine.valeur(UPDATED_VALEUR).date(UPDATED_DATE);

        restMesureAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesureAlbumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesureAlbumine))
            )
            .andExpect(status().isOk());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
        MesureAlbumine testMesureAlbumine = mesureAlbumineList.get(mesureAlbumineList.size() - 1);
        assertThat(testMesureAlbumine.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testMesureAlbumine.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mesureAlbumine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isBadRequest());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMesureAlbumine() throws Exception {
        int databaseSizeBeforeUpdate = mesureAlbumineRepository.findAll().size();
        mesureAlbumine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesureAlbumineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mesureAlbumine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MesureAlbumine in the database
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMesureAlbumine() throws Exception {
        // Initialize the database
        mesureAlbumineRepository.saveAndFlush(mesureAlbumine);

        int databaseSizeBeforeDelete = mesureAlbumineRepository.findAll().size();

        // Delete the mesureAlbumine
        restMesureAlbumineMockMvc
            .perform(delete(ENTITY_API_URL_ID, mesureAlbumine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MesureAlbumine> mesureAlbumineList = mesureAlbumineRepository.findAll();
        assertThat(mesureAlbumineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
