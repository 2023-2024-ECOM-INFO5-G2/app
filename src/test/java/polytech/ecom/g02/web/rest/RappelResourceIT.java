package polytech.ecom.g02.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static polytech.ecom.g02.web.rest.TestUtil.sameInstant;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import polytech.ecom.g02.IntegrationTest;
import polytech.ecom.g02.domain.Rappel;
import polytech.ecom.g02.repository.RappelRepository;

/**
 * Integration tests for the {@link RappelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RappelResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ECHEANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ECHEANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_INTERVALE_JOURS = 1;
    private static final Integer UPDATED_INTERVALE_JOURS = 2;

    private static final String DEFAULT_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_TACHE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FEE_DANS_LETANG = false;
    private static final Boolean UPDATED_FEE_DANS_LETANG = true;

    private static final String ENTITY_API_URL = "/api/rappels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RappelRepository rappelRepository;

    @Mock
    private RappelRepository rappelRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRappelMockMvc;

    private Rappel rappel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rappel createEntity(EntityManager em) {
        Rappel rappel = new Rappel()
            .date(DEFAULT_DATE)
            .echeance(DEFAULT_ECHEANCE)
            .intervaleJours(DEFAULT_INTERVALE_JOURS)
            .tache(DEFAULT_TACHE)
            .feeDansLetang(DEFAULT_FEE_DANS_LETANG);
        return rappel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rappel createUpdatedEntity(EntityManager em) {
        Rappel rappel = new Rappel()
            .date(UPDATED_DATE)
            .echeance(UPDATED_ECHEANCE)
            .intervaleJours(UPDATED_INTERVALE_JOURS)
            .tache(UPDATED_TACHE)
            .feeDansLetang(UPDATED_FEE_DANS_LETANG);
        return rappel;
    }

    @BeforeEach
    public void initTest() {
        rappel = createEntity(em);
    }

    @Test
    @Transactional
    void createRappel() throws Exception {
        int databaseSizeBeforeCreate = rappelRepository.findAll().size();
        // Create the Rappel
        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isCreated());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeCreate + 1);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRappel.getEcheance()).isEqualTo(DEFAULT_ECHEANCE);
        assertThat(testRappel.getIntervaleJours()).isEqualTo(DEFAULT_INTERVALE_JOURS);
        assertThat(testRappel.getTache()).isEqualTo(DEFAULT_TACHE);
        assertThat(testRappel.getFeeDansLetang()).isEqualTo(DEFAULT_FEE_DANS_LETANG);
    }

    @Test
    @Transactional
    void createRappelWithExistingId() throws Exception {
        // Create the Rappel with an existing ID
        rappel.setId(1L);

        int databaseSizeBeforeCreate = rappelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setDate(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEcheanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setEcheance(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntervaleJoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setIntervaleJours(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTacheIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setTache(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeeDansLetangIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setFeeDansLetang(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRappels() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        // Get all the rappelList
        restRappelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rappel.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].echeance").value(hasItem(sameInstant(DEFAULT_ECHEANCE))))
            .andExpect(jsonPath("$.[*].intervaleJours").value(hasItem(DEFAULT_INTERVALE_JOURS)))
            .andExpect(jsonPath("$.[*].tache").value(hasItem(DEFAULT_TACHE)))
            .andExpect(jsonPath("$.[*].feeDansLetang").value(hasItem(DEFAULT_FEE_DANS_LETANG.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRappelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rappelRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRappelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rappelRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRappelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rappelRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRappelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rappelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        // Get the rappel
        restRappelMockMvc
            .perform(get(ENTITY_API_URL_ID, rappel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rappel.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.echeance").value(sameInstant(DEFAULT_ECHEANCE)))
            .andExpect(jsonPath("$.intervaleJours").value(DEFAULT_INTERVALE_JOURS))
            .andExpect(jsonPath("$.tache").value(DEFAULT_TACHE))
            .andExpect(jsonPath("$.feeDansLetang").value(DEFAULT_FEE_DANS_LETANG.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRappel() throws Exception {
        // Get the rappel
        restRappelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel
        Rappel updatedRappel = rappelRepository.findById(rappel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRappel are not directly saved in db
        em.detach(updatedRappel);
        updatedRappel
            .date(UPDATED_DATE)
            .echeance(UPDATED_ECHEANCE)
            .intervaleJours(UPDATED_INTERVALE_JOURS)
            .tache(UPDATED_TACHE)
            .feeDansLetang(UPDATED_FEE_DANS_LETANG);

        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRappel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getIntervaleJours()).isEqualTo(UPDATED_INTERVALE_JOURS);
        assertThat(testRappel.getTache()).isEqualTo(UPDATED_TACHE);
        assertThat(testRappel.getFeeDansLetang()).isEqualTo(UPDATED_FEE_DANS_LETANG);
    }

    @Test
    @Transactional
    void putNonExistingRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rappel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRappelWithPatch() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel using partial update
        Rappel partialUpdatedRappel = new Rappel();
        partialUpdatedRappel.setId(rappel.getId());

        partialUpdatedRappel.date(UPDATED_DATE).echeance(UPDATED_ECHEANCE).intervaleJours(UPDATED_INTERVALE_JOURS).tache(UPDATED_TACHE);

        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getIntervaleJours()).isEqualTo(UPDATED_INTERVALE_JOURS);
        assertThat(testRappel.getTache()).isEqualTo(UPDATED_TACHE);
        assertThat(testRappel.getFeeDansLetang()).isEqualTo(DEFAULT_FEE_DANS_LETANG);
    }

    @Test
    @Transactional
    void fullUpdateRappelWithPatch() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel using partial update
        Rappel partialUpdatedRappel = new Rappel();
        partialUpdatedRappel.setId(rappel.getId());

        partialUpdatedRappel
            .date(UPDATED_DATE)
            .echeance(UPDATED_ECHEANCE)
            .intervaleJours(UPDATED_INTERVALE_JOURS)
            .tache(UPDATED_TACHE)
            .feeDansLetang(UPDATED_FEE_DANS_LETANG);

        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getIntervaleJours()).isEqualTo(UPDATED_INTERVALE_JOURS);
        assertThat(testRappel.getTache()).isEqualTo(UPDATED_TACHE);
        assertThat(testRappel.getFeeDansLetang()).isEqualTo(UPDATED_FEE_DANS_LETANG);
    }

    @Test
    @Transactional
    void patchNonExistingRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeDelete = rappelRepository.findAll().size();

        // Delete the rappel
        restRappelMockMvc
            .perform(delete(ENTITY_API_URL_ID, rappel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
