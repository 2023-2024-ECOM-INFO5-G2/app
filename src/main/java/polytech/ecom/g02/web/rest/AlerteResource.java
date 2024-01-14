package polytech.ecom.g02.web.rest;

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
import polytech.ecom.g02.domain.Alerte;
import polytech.ecom.g02.repository.AlerteRepository;
import polytech.ecom.g02.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.ecom.g02.domain.Alerte}.
 */
@RestController
@RequestMapping("/api/alertes")
@Transactional
public class AlerteResource {

    private final Logger log = LoggerFactory.getLogger(AlerteResource.class);

    private static final String ENTITY_NAME = "alerte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteRepository alerteRepository;

    public AlerteResource(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    /**
     * {@code POST  /alertes} : Create a new alerte.
     *
     * @param alerte the alerte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerte, or with status {@code 400 (Bad Request)} if the alerte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Alerte> createAlerte(@Valid @RequestBody Alerte alerte) throws URISyntaxException {
        log.debug("REST request to save Alerte : {}", alerte);
        if (alerte.getId() != null) {
            throw new BadRequestAlertException("A new alerte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alerte result = alerteRepository.save(alerte);
        return ResponseEntity
            .created(new URI("/api/alertes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alertes/:id} : Updates an existing alerte.
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alerte> updateAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Alerte alerte
    ) throws URISyntaxException {
        log.debug("REST request to update Alerte : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Alerte result = alerteRepository.save(alerte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alertes/:id} : Partial updates given fields of an existing alerte, field will ignore if it is null
     *
     * @param id the id of the alerte to save.
     * @param alerte the alerte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerte,
     * or with status {@code 400 (Bad Request)} if the alerte is not valid,
     * or with status {@code 404 (Not Found)} if the alerte is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alerte> partialUpdateAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Alerte alerte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alerte partially : {}, {}", id, alerte);
        if (alerte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alerte> result = alerteRepository
            .findById(alerte.getId())
            .map(existingAlerte -> {
                if (alerte.getDescription() != null) {
                    existingAlerte.setDescription(alerte.getDescription());
                }
                if (alerte.getDate() != null) {
                    existingAlerte.setDate(alerte.getDate());
                }
                if (alerte.getSevere() != null) {
                    existingAlerte.setSevere(alerte.getSevere());
                }
                if (alerte.getCode() != null) {
                    existingAlerte.setCode(alerte.getCode());
                }

                return existingAlerte;
            })
            .map(alerteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerte.getId().toString())
        );
    }

    /**
     * {@code GET  /alertes} : get all the alertes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertes in body.
     */
    @GetMapping("")
    public List<Alerte> getAllAlertes() {
        log.debug("REST request to get all Alertes");
        return alerteRepository.findAll();
    }

    /**
     * {@code GET  /alertes/:id} : get the "id" alerte.
     *
     * @param id the id of the alerte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alerte> getAlerte(@PathVariable Long id) {
        log.debug("REST request to get Alerte : {}", id);
        Optional<Alerte> alerte = alerteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alerte);
    }

    /**
     * {@code DELETE  /alertes/:id} : delete the "id" alerte.
     *
     * @param id the id of the alerte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerte(@PathVariable Long id) {
        log.debug("REST request to delete Alerte : {}", id);
        alerteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
