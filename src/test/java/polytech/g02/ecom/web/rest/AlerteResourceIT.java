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
import polytech.g02.ecom.domain.Alerte;
import polytech.g02.ecom.repository.AlerteRepository;

/**
 * Integration tests for the {@link AlerteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlerteResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlerteRepository alerteRepository;

    @Mock
    private AlerteRepository alerteRepositoryMock;

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
        Alerte alerte = new Alerte().description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE);
        return alerte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createUpdatedEntity(EntityManager em) {
        Alerte alerte = new Alerte().description(UPDATED_DESCRIPTION).date(UPDATED_DATE);
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlertesWithEagerRelationshipsIsEnabled() throws Exception {
        when(alerteRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlerteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alerteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlertesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alerteRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlerteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alerteRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
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
        updatedAlerte.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

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

        partialUpdatedAlerte.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

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

        partialUpdatedAlerte.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

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
