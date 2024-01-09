package polytech.g02.ecom.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import polytech.g02.ecom.domain.Rappel;
import polytech.g02.ecom.repository.RappelRepository;
import polytech.g02.ecom.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.g02.ecom.domain.Rappel}.
 */
@RestController
@RequestMapping("/api/rappels")
@Transactional
public class RappelResource {

    private final Logger log = LoggerFactory.getLogger(RappelResource.class);

    private static final String ENTITY_NAME = "rappel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RappelRepository rappelRepository;

    public RappelResource(RappelRepository rappelRepository) {
        this.rappelRepository = rappelRepository;
    }

    /**
     * {@code POST  /rappels} : Create a new rappel.
     *
     * @param rappel the rappel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rappel, or with status {@code 400 (Bad Request)} if the rappel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Rappel> createRappel(@Valid @RequestBody Rappel rappel) throws URISyntaxException {
        log.debug("REST request to save Rappel : {}", rappel);
        if (rappel.getId() != null) {
            throw new BadRequestAlertException("A new rappel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rappel result = rappelRepository.save(rappel);
        return ResponseEntity
            .created(new URI("/api/rappels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rappels/:id} : Updates an existing rappel.
     *
     * @param id the id of the rappel to save.
     * @param rappel the rappel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rappel,
     * or with status {@code 400 (Bad Request)} if the rappel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rappel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rappel> updateRappel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rappel rappel
    ) throws URISyntaxException {
        log.debug("REST request to update Rappel : {}, {}", id, rappel);
        if (rappel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rappel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rappelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rappel result = rappelRepository.save(rappel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rappel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rappels/:id} : Partial updates given fields of an existing rappel, field will ignore if it is null
     *
     * @param id the id of the rappel to save.
     * @param rappel the rappel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rappel,
     * or with status {@code 400 (Bad Request)} if the rappel is not valid,
     * or with status {@code 404 (Not Found)} if the rappel is not found,
     * or with status {@code 500 (Internal Server Error)} if the rappel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rappel> partialUpdateRappel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rappel rappel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rappel partially : {}, {}", id, rappel);
        if (rappel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rappel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rappelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rappel> result = rappelRepository
            .findById(rappel.getId())
            .map(existingRappel -> {
                if (rappel.getFrequence() != null) {
                    existingRappel.setFrequence(rappel.getFrequence());
                }
                if (rappel.getEcheance() != null) {
                    existingRappel.setEcheance(rappel.getEcheance());
                }
                if (rappel.getTache() != null) {
                    existingRappel.setTache(rappel.getTache());
                }

                return existingRappel;
            })
            .map(rappelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rappel.getId().toString())
        );
    }

    /**
     * {@code GET  /rappels} : get all the rappels.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rappels in body.
     */
    @GetMapping("")
    public List<Rappel> getAllRappels(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Rappels");
        if (eagerload) {
            return rappelRepository.findAllWithEagerRelationships();
        } else {
            return rappelRepository.findAll();
        }
    }

    /**
     * {@code GET  /rappels/:id} : get the "id" rappel.
     *
     * @param id the id of the rappel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rappel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rappel> getRappel(@PathVariable Long id) {
        log.debug("REST request to get Rappel : {}", id);
        Optional<Rappel> rappel = rappelRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(rappel);
    }

    /**
     * {@code DELETE  /rappels/:id} : delete the "id" rappel.
     *
     * @param id the id of the rappel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRappel(@PathVariable Long id) {
        log.debug("REST request to delete Rappel : {}", id);
        rappelRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
