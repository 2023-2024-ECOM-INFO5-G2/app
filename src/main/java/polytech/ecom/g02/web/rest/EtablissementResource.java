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
import polytech.ecom.g02.domain.Etablissement;
import polytech.ecom.g02.repository.EtablissementRepository;
import polytech.ecom.g02.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.ecom.g02.domain.Etablissement}.
 */
@RestController
@RequestMapping("/api/etablissements")
@Transactional
public class EtablissementResource {

    private final Logger log = LoggerFactory.getLogger(EtablissementResource.class);

    private static final String ENTITY_NAME = "etablissement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtablissementRepository etablissementRepository;

    public EtablissementResource(EtablissementRepository etablissementRepository) {
        this.etablissementRepository = etablissementRepository;
    }

    /**
     * {@code POST  /etablissements} : Create a new etablissement.
     *
     * @param etablissement the etablissement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etablissement, or with status {@code 400 (Bad Request)} if the etablissement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Etablissement> createEtablissement(@Valid @RequestBody Etablissement etablissement) throws URISyntaxException {
        log.debug("REST request to save Etablissement : {}", etablissement);
        if (etablissement.getId() != null) {
            throw new BadRequestAlertException("A new etablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etablissement result = etablissementRepository.save(etablissement);
        return ResponseEntity
            .created(new URI("/api/etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etablissements/:id} : Updates an existing etablissement.
     *
     * @param id the id of the etablissement to save.
     * @param etablissement the etablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etablissement,
     * or with status {@code 400 (Bad Request)} if the etablissement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Etablissement> updateEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Etablissement etablissement
    ) throws URISyntaxException {
        log.debug("REST request to update Etablissement : {}, {}", id, etablissement);
        if (etablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Etablissement result = etablissementRepository.save(etablissement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etablissement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etablissements/:id} : Partial updates given fields of an existing etablissement, field will ignore if it is null
     *
     * @param id the id of the etablissement to save.
     * @param etablissement the etablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etablissement,
     * or with status {@code 400 (Bad Request)} if the etablissement is not valid,
     * or with status {@code 404 (Not Found)} if the etablissement is not found,
     * or with status {@code 500 (Internal Server Error)} if the etablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Etablissement> partialUpdateEtablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Etablissement etablissement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etablissement partially : {}, {}", id, etablissement);
        if (etablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Etablissement> result = etablissementRepository
            .findById(etablissement.getId())
            .map(existingEtablissement -> {
                if (etablissement.getNom() != null) {
                    existingEtablissement.setNom(etablissement.getNom());
                }
                if (etablissement.getAdresse() != null) {
                    existingEtablissement.setAdresse(etablissement.getAdresse());
                }
                if (etablissement.getVille() != null) {
                    existingEtablissement.setVille(etablissement.getVille());
                }
                if (etablissement.getCodePostal() != null) {
                    existingEtablissement.setCodePostal(etablissement.getCodePostal());
                }

                return existingEtablissement;
            })
            .map(etablissementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etablissement.getId().toString())
        );
    }

    /**
     * {@code GET  /etablissements} : get all the etablissements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etablissements in body.
     */
    @GetMapping("")
    public List<Etablissement> getAllEtablissements(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Etablissements");
        if (eagerload) {
            return etablissementRepository.findAllWithEagerRelationships();
        } else {
            return etablissementRepository.findAll();
        }
    }

    /**
     * {@code GET  /etablissements/:id} : get the "id" etablissement.
     *
     * @param id the id of the etablissement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etablissement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Etablissement> getEtablissement(@PathVariable Long id) {
        log.debug("REST request to get Etablissement : {}", id);
        Optional<Etablissement> etablissement = etablissementRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(etablissement);
    }

    /**
     * {@code DELETE  /etablissements/:id} : delete the "id" etablissement.
     *
     * @param id the id of the etablissement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete Etablissement : {}", id);
        etablissementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
