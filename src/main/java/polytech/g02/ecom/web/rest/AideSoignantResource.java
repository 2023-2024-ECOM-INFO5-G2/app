package polytech.g02.ecom.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import polytech.g02.ecom.domain.AideSoignant;
import polytech.g02.ecom.repository.AideSoignantRepository;
import polytech.g02.ecom.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.g02.ecom.domain.AideSoignant}.
 */
@RestController
@RequestMapping("/api/aide-soignants")
@Transactional
public class AideSoignantResource {

    private final Logger log = LoggerFactory.getLogger(AideSoignantResource.class);

    private static final String ENTITY_NAME = "aideSoignant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AideSoignantRepository aideSoignantRepository;

    public AideSoignantResource(AideSoignantRepository aideSoignantRepository) {
        this.aideSoignantRepository = aideSoignantRepository;
    }

    /**
     * {@code POST  /aide-soignants} : Create a new aideSoignant.
     *
     * @param aideSoignant the aideSoignant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aideSoignant, or with status {@code 400 (Bad Request)} if the aideSoignant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AideSoignant> createAideSoignant(@RequestBody AideSoignant aideSoignant) throws URISyntaxException {
        log.debug("REST request to save AideSoignant : {}", aideSoignant);
        if (aideSoignant.getId() != null) {
            throw new BadRequestAlertException("A new aideSoignant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AideSoignant result = aideSoignantRepository.save(aideSoignant);
        return ResponseEntity
            .created(new URI("/api/aide-soignants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aide-soignants/:id} : Updates an existing aideSoignant.
     *
     * @param id the id of the aideSoignant to save.
     * @param aideSoignant the aideSoignant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aideSoignant,
     * or with status {@code 400 (Bad Request)} if the aideSoignant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aideSoignant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AideSoignant> updateAideSoignant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AideSoignant aideSoignant
    ) throws URISyntaxException {
        log.debug("REST request to update AideSoignant : {}, {}", id, aideSoignant);
        if (aideSoignant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aideSoignant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aideSoignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AideSoignant result = aideSoignantRepository.save(aideSoignant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aideSoignant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aide-soignants/:id} : Partial updates given fields of an existing aideSoignant, field will ignore if it is null
     *
     * @param id the id of the aideSoignant to save.
     * @param aideSoignant the aideSoignant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aideSoignant,
     * or with status {@code 400 (Bad Request)} if the aideSoignant is not valid,
     * or with status {@code 404 (Not Found)} if the aideSoignant is not found,
     * or with status {@code 500 (Internal Server Error)} if the aideSoignant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AideSoignant> partialUpdateAideSoignant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AideSoignant aideSoignant
    ) throws URISyntaxException {
        log.debug("REST request to partial update AideSoignant partially : {}, {}", id, aideSoignant);
        if (aideSoignant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aideSoignant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aideSoignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AideSoignant> result = aideSoignantRepository.findById(aideSoignant.getId()).map(aideSoignantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aideSoignant.getId().toString())
        );
    }

    /**
     * {@code GET  /aide-soignants} : get all the aideSoignants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aideSoignants in body.
     */
    @GetMapping("")
    public List<AideSoignant> getAllAideSoignants(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all AideSoignants");
        if (eagerload) {
            return aideSoignantRepository.findAllWithEagerRelationships();
        } else {
            return aideSoignantRepository.findAll();
        }
    }

    /**
     * {@code GET  /aide-soignants/:id} : get the "id" aideSoignant.
     *
     * @param id the id of the aideSoignant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aideSoignant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AideSoignant> getAideSoignant(@PathVariable Long id) {
        log.debug("REST request to get AideSoignant : {}", id);
        Optional<AideSoignant> aideSoignant = aideSoignantRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(aideSoignant);
    }

    /**
     * {@code DELETE  /aide-soignants/:id} : delete the "id" aideSoignant.
     *
     * @param id the id of the aideSoignant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAideSoignant(@PathVariable Long id) {
        log.debug("REST request to delete AideSoignant : {}", id);
        aideSoignantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
