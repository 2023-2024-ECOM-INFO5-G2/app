package polytech.g02.ecom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import polytech.g02.ecom.IntegrationTest;
import polytech.g02.ecom.domain.Rappel;
import polytech.g02.ecom.repository.RappelRepository;

/**
 * Integration tests for the {@link RappelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RappelResourceIT {

    private static final String DEFAULT_FREQUENCE = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ECHEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ECHEANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_TACHE = "BBBBBBBBBB";

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
        Rappel rappel = new Rappel().frequence(DEFAULT_FREQUENCE).echeance(DEFAULT_ECHEANCE).tache(DEFAULT_TACHE);
        return rappel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rappel createUpdatedEntity(EntityManager em) {
        Rappel rappel = new Rappel().frequence(UPDATED_FREQUENCE).echeance(UPDATED_ECHEANCE).tache(UPDATED_TACHE);
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
        assertThat(testRappel.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testRappel.getEcheance()).isEqualTo(DEFAULT_ECHEANCE);
        assertThat(testRappel.getTache()).isEqualTo(DEFAULT_TACHE);
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
    void checkFrequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setFrequence(null);

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
    void getAllRappels() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        // Get all the rappelList
        restRappelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rappel.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE)))
            .andExpect(jsonPath("$.[*].echeance").value(hasItem(DEFAULT_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].tache").value(hasItem(DEFAULT_TACHE)));
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
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE))
            .andExpect(jsonPath("$.echeance").value(DEFAULT_ECHEANCE.toString()))
            .andExpect(jsonPath("$.tache").value(DEFAULT_TACHE));
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
        updatedRappel.frequence(UPDATED_FREQUENCE).echeance(UPDATED_ECHEANCE).tache(UPDATED_TACHE);

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
        assertThat(testRappel.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getTache()).isEqualTo(UPDATED_TACHE);
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

        partialUpdatedRappel.echeance(UPDATED_ECHEANCE);

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
        assertThat(testRappel.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getTache()).isEqualTo(DEFAULT_TACHE);
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

        partialUpdatedRappel.frequence(UPDATED_FREQUENCE).echeance(UPDATED_ECHEANCE).tache(UPDATED_TACHE);

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
        assertThat(testRappel.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testRappel.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testRappel.getTache()).isEqualTo(UPDATED_TACHE);
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
