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
import polytech.g02.ecom.domain.Infirmiere;
import polytech.g02.ecom.repository.InfirmiereRepository;
import polytech.g02.ecom.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.g02.ecom.domain.Infirmiere}.
 */
@RestController
@RequestMapping("/api/infirmieres")
@Transactional
public class InfirmiereResource {

    private final Logger log = LoggerFactory.getLogger(InfirmiereResource.class);

    private static final String ENTITY_NAME = "infirmiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfirmiereRepository infirmiereRepository;

    public InfirmiereResource(InfirmiereRepository infirmiereRepository) {
        this.infirmiereRepository = infirmiereRepository;
    }

    /**
     * {@code POST  /infirmieres} : Create a new infirmiere.
     *
     * @param infirmiere the infirmiere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infirmiere, or with status {@code 400 (Bad Request)} if the infirmiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Infirmiere> createInfirmiere(@RequestBody Infirmiere infirmiere) throws URISyntaxException {
        log.debug("REST request to save Infirmiere : {}", infirmiere);
        if (infirmiere.getId() != null) {
            throw new BadRequestAlertException("A new infirmiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Infirmiere result = infirmiereRepository.save(infirmiere);
        return ResponseEntity
            .created(new URI("/api/infirmieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infirmieres/:id} : Updates an existing infirmiere.
     *
     * @param id the id of the infirmiere to save.
     * @param infirmiere the infirmiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infirmiere,
     * or with status {@code 400 (Bad Request)} if the infirmiere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infirmiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Infirmiere> updateInfirmiere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Infirmiere infirmiere
    ) throws URISyntaxException {
        log.debug("REST request to update Infirmiere : {}, {}", id, infirmiere);
        if (infirmiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infirmiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infirmiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Infirmiere result = infirmiereRepository.save(infirmiere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infirmiere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infirmieres/:id} : Partial updates given fields of an existing infirmiere, field will ignore if it is null
     *
     * @param id the id of the infirmiere to save.
     * @param infirmiere the infirmiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infirmiere,
     * or with status {@code 400 (Bad Request)} if the infirmiere is not valid,
     * or with status {@code 404 (Not Found)} if the infirmiere is not found,
     * or with status {@code 500 (Internal Server Error)} if the infirmiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Infirmiere> partialUpdateInfirmiere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Infirmiere infirmiere
    ) throws URISyntaxException {
        log.debug("REST request to partial update Infirmiere partially : {}, {}", id, infirmiere);
        if (infirmiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infirmiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infirmiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Infirmiere> result = infirmiereRepository.findById(infirmiere.getId()).map(infirmiereRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infirmiere.getId().toString())
        );
    }

    /**
     * {@code GET  /infirmieres} : get all the infirmieres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infirmieres in body.
     */
    @GetMapping("")
    public List<Infirmiere> getAllInfirmieres(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Infirmieres");
        if (eagerload) {
            return infirmiereRepository.findAllWithEagerRelationships();
        } else {
            return infirmiereRepository.findAll();
        }
    }

    /**
     * {@code GET  /infirmieres/:id} : get the "id" infirmiere.
     *
     * @param id the id of the infirmiere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infirmiere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Infirmiere> getInfirmiere(@PathVariable Long id) {
        log.debug("REST request to get Infirmiere : {}", id);
        Optional<Infirmiere> infirmiere = infirmiereRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(infirmiere);
    }

    /**
     * {@code DELETE  /infirmieres/:id} : delete the "id" infirmiere.
     *
     * @param id the id of the infirmiere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfirmiere(@PathVariable Long id) {
        log.debug("REST request to delete Infirmiere : {}", id);
        infirmiereRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
