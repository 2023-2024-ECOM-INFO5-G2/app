package polytech.ecom.g02.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import polytech.ecom.g02.domain.Alerte;
import polytech.ecom.g02.domain.MesureAlbumine;
import polytech.ecom.g02.domain.Patient;
import polytech.ecom.g02.repository.AlerteRepository;
import polytech.ecom.g02.repository.MesureAlbumineRepository;
import polytech.ecom.g02.repository.PatientRepository;
import polytech.ecom.g02.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.ecom.g02.domain.MesureAlbumine}.
 */
@RestController
@RequestMapping("/api/mesure-albumines")
@Transactional
public class MesureAlbumineResource {

    private final Logger log = LoggerFactory.getLogger(MesureAlbumineResource.class);

    private static final String ENTITY_NAME = "mesureAlbumine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MesureAlbumineRepository mesureAlbumineRepository;
    private final AlerteRepository alerteRepository;
    private final PatientRepository patientRepository;

    public MesureAlbumineResource(
        MesureAlbumineRepository mesureAlbumineRepository,
        AlerteRepository alerteRepository,
        PatientRepository patientRepository
    ) {
        this.mesureAlbumineRepository = mesureAlbumineRepository;
        this.alerteRepository = alerteRepository;
        this.patientRepository = patientRepository;
    }

    private boolean isNewest(Patient patient, ZonedDateTime date) {
        Set<MesureAlbumine> mesures = patient.getMesureAlbumines();

        for (MesureAlbumine mesure : mesures) {
            if (mesure.getDate().isAfter(date)) return false;
        }
        return true;
    }

    private void check(MesureAlbumine mesureAlbumine) {
        Patient patient = patientRepository.getReferenceById(mesureAlbumine.getPatient().getId()).addMesureAlbumine(mesureAlbumine);
        if (!isNewest(patient, mesureAlbumine.getDate())) return;
        Set<Alerte> alertes = patient.getAlertes();
        if (alertes != null) {
            for (Alerte alerte : alertes) {
                if (alerte.getMesureAlbumine() != null) {
                    alerteRepository.deleteById(alerte.getId());
                    patient.removeAlerte(alerte);
                }
            }
        }
        if (mesureAlbumine.getValeur() < 35) {
            Alerte alerte = new Alerte();
            alerte.setPatient(mesureAlbumine.getPatient());
            alerte.setDate(mesureAlbumine.getDate());
            if (mesureAlbumine.getValeur() < 30) {
                alerte.setSevere(true);
                alerte.setMesureAlbumine(mesureAlbumine);
                alerte.setCode(31);
                alerte.setDescription("Alerte Albumine très faible : " + mesureAlbumine.getValeur());
            } else {
                alerte.setSevere(false);
                alerte.setMesureAlbumine(mesureAlbumine);
                alerte.setCode(30);
                alerte.setDescription("Attention Albumine faible : " + mesureAlbumine.getValeur());
            }
            alerteRepository.save(alerte);
        }
        patientRepository.save(patient);
    }

    /**
     * {@code POST  /mesure-albumines} : Create a new mesureAlbumine.
     *
     * @param mesureAlbumine the mesureAlbumine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mesureAlbumine, or with status {@code 400 (Bad Request)} if the mesureAlbumine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MesureAlbumine> createMesureAlbumine(@Valid @RequestBody MesureAlbumine mesureAlbumine)
        throws URISyntaxException {
        log.debug("REST request to save MesureAlbumine : {}", mesureAlbumine);
        if (mesureAlbumine.getId() != null) {
            throw new BadRequestAlertException("A new mesureAlbumine cannot already have an ID", ENTITY_NAME, "idexists");
        }

        MesureAlbumine result = mesureAlbumineRepository.save(mesureAlbumine);
        check(mesureAlbumine);

        return ResponseEntity
            .created(new URI("/api/mesure-albumines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mesure-albumines/:id} : Updates an existing mesureAlbumine.
     *
     * @param id the id of the mesureAlbumine to save.
     * @param mesureAlbumine the mesureAlbumine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesureAlbumine,
     * or with status {@code 400 (Bad Request)} if the mesureAlbumine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mesureAlbumine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MesureAlbumine> updateMesureAlbumine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MesureAlbumine mesureAlbumine
    ) throws URISyntaxException {
        log.debug("REST request to update MesureAlbumine : {}, {}", id, mesureAlbumine);
        if (mesureAlbumine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesureAlbumine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureAlbumineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            alerteRepository.deleteById(mesureAlbumineRepository.getReferenceById(mesureAlbumine.getId()).getAlerte().getId());
        } catch (Exception e) {
            // Nothing to do here
        }

        MesureAlbumine result = mesureAlbumineRepository.save(mesureAlbumine);
        check(mesureAlbumine);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesureAlbumine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mesure-albumines/:id} : Partial updates given fields of an existing mesureAlbumine, field will ignore if it is null
     *
     * @param id the id of the mesureAlbumine to save.
     * @param mesureAlbumine the mesureAlbumine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesureAlbumine,
     * or with status {@code 400 (Bad Request)} if the mesureAlbumine is not valid,
     * or with status {@code 404 (Not Found)} if the mesureAlbumine is not found,
     * or with status {@code 500 (Internal Server Error)} if the mesureAlbumine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MesureAlbumine> partialUpdateMesureAlbumine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MesureAlbumine mesureAlbumine
    ) throws URISyntaxException {
        log.debug("REST request to partial update MesureAlbumine partially : {}, {}", id, mesureAlbumine);
        if (mesureAlbumine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesureAlbumine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureAlbumineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MesureAlbumine> result = mesureAlbumineRepository
            .findById(mesureAlbumine.getId())
            .map(existingMesureAlbumine -> {
                if (mesureAlbumine.getValeur() != null) {
                    existingMesureAlbumine.setValeur(mesureAlbumine.getValeur());
                }
                if (mesureAlbumine.getDate() != null) {
                    existingMesureAlbumine.setDate(mesureAlbumine.getDate());
                }

                return existingMesureAlbumine;
            })
            .map(mesureAlbumineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesureAlbumine.getId().toString())
        );
    }

    /**
     * {@code GET  /mesure-albumines} : get all the mesureAlbumines.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mesureAlbumines in body.
     */
    @GetMapping("")
    public List<MesureAlbumine> getAllMesureAlbumines(@RequestParam(required = false) String filter) {
        if ("alerte-is-null".equals(filter)) {
            log.debug("REST request to get all MesureAlbumines where alerte is null");
            return StreamSupport
                .stream(mesureAlbumineRepository.findAll().spliterator(), false)
                .filter(mesureAlbumine -> mesureAlbumine.getAlerte() == null)
                .toList();
        }
        log.debug("REST request to get all MesureAlbumines");
        return mesureAlbumineRepository.findAll();
    }

    /**
     * {@code GET  /mesure-albumines/:id} : get the "id" mesureAlbumine.
     *
     * @param id the id of the mesureAlbumine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mesureAlbumine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MesureAlbumine> getMesureAlbumine(@PathVariable Long id) {
        log.debug("REST request to get MesureAlbumine : {}", id);
        Optional<MesureAlbumine> mesureAlbumine = mesureAlbumineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mesureAlbumine);
    }

    /**
     * {@code DELETE  /mesure-albumines/:id} : delete the "id" mesureAlbumine.
     *
     * @param id the id of the mesureAlbumine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesureAlbumine(@PathVariable Long id) {
        log.debug("REST request to delete MesureAlbumine : {}", id);
        try {
            alerteRepository.deleteById(mesureAlbumineRepository.getReferenceById(id).getAlerte().getId());
        } catch (Exception e) {
            // Nothing to do here
        }
        mesureAlbumineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
