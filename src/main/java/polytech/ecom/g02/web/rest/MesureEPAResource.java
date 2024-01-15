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
import polytech.ecom.g02.domain.MesureEPA;
import polytech.ecom.g02.domain.Patient;
import polytech.ecom.g02.repository.AlerteRepository;
import polytech.ecom.g02.repository.MesureEPARepository;
import polytech.ecom.g02.repository.PatientRepository;
import polytech.ecom.g02.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.ecom.g02.domain.MesureEPA}.
 */
@RestController
@RequestMapping("/api/mesure-epas")
@Transactional
public class MesureEPAResource {

    private final Logger log = LoggerFactory.getLogger(MesureEPAResource.class);

    private static final String ENTITY_NAME = "mesureEPA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MesureEPARepository mesureEPARepository;
    private final AlerteRepository alerteRepository;
    private final PatientRepository patientRepository;

    public MesureEPAResource(
        MesureEPARepository mesureEPARepository,
        AlerteRepository alerteRepository,
        PatientRepository patientRepository
    ) {
        this.mesureEPARepository = mesureEPARepository;
        this.alerteRepository = alerteRepository;
        this.patientRepository = patientRepository;
    }

    private boolean isNewest(Patient patient, ZonedDateTime date) {
        Set<MesureEPA> mesures = patient.getMesureEPAS();

        for (MesureEPA mesure : mesures) {
            if (mesure.getDate().isAfter(date)) return false;
        }
        return true;
    }

    private void check(MesureEPA mesureEPA) {
        Patient patient = patientRepository.getReferenceById(mesureEPA.getPatient().getId()).addMesureEPA(mesureEPA);
        if (!isNewest(patient, mesureEPA.getDate())) return;
        Set<Alerte> alertes = patient.getAlertes();
        if (alertes != null) {
            for (Alerte alerte : alertes) {
                if (alerte.getMesureEPA() != null) {
                    alerteRepository.deleteById(alerte.getId());
                    patient.removeAlerte(alerte);
                }
            }
        }
        if (mesureEPA.getValeur() < 7) {
            Alerte alerte = new Alerte();
            alerte.setMesureEPA(mesureEPA);
            alerte.setSevere(false);
            alerte.setPatient(patient);
            alerte.setCode(40);
            alerte.setDescription("Attention EPA faible : " + mesureEPA.getValeur());
            alerte.setDate(mesureEPA.getDate());
            patient.addAlerte(alerte);
            alerteRepository.save(alerte);
        }
        patientRepository.save(patient);
    }

    /**
     * {@code POST  /mesure-epas} : Create a new mesureEPA.
     *
     * @param mesureEPA the mesureEPA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mesureEPA, or with status {@code 400 (Bad Request)} if the mesureEPA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MesureEPA> createMesureEPA(@Valid @RequestBody MesureEPA mesureEPA) throws URISyntaxException {
        log.debug("REST request to save MesureEPA : {}", mesureEPA);
        if (mesureEPA.getId() != null) {
            throw new BadRequestAlertException("A new mesureEPA cannot already have an ID", ENTITY_NAME, "idexists");
        }

        MesureEPA result = mesureEPARepository.save(mesureEPA);
        check(mesureEPA);

        return ResponseEntity
            .created(new URI("/api/mesure-epas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mesure-epas/:id} : Updates an existing mesureEPA.
     *
     * @param id the id of the mesureEPA to save.
     * @param mesureEPA the mesureEPA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesureEPA,
     * or with status {@code 400 (Bad Request)} if the mesureEPA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mesureEPA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MesureEPA> updateMesureEPA(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MesureEPA mesureEPA
    ) throws URISyntaxException {
        log.debug("REST request to update MesureEPA : {}, {}", id, mesureEPA);
        if (mesureEPA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesureEPA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureEPARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            alerteRepository.deleteById(mesureEPARepository.getReferenceById(mesureEPA.getId()).getAlerte().getId());
        } catch (Exception e) {
            //Nothing to do here
        }

        MesureEPA result = mesureEPARepository.save(mesureEPA);
        check(mesureEPA);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesureEPA.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mesure-epas/:id} : Partial updates given fields of an existing mesureEPA, field will ignore if it is null
     *
     * @param id the id of the mesureEPA to save.
     * @param mesureEPA the mesureEPA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesureEPA,
     * or with status {@code 400 (Bad Request)} if the mesureEPA is not valid,
     * or with status {@code 404 (Not Found)} if the mesureEPA is not found,
     * or with status {@code 500 (Internal Server Error)} if the mesureEPA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MesureEPA> partialUpdateMesureEPA(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MesureEPA mesureEPA
    ) throws URISyntaxException {
        log.debug("REST request to partial update MesureEPA partially : {}, {}", id, mesureEPA);
        if (mesureEPA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesureEPA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesureEPARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MesureEPA> result = mesureEPARepository
            .findById(mesureEPA.getId())
            .map(existingMesureEPA -> {
                if (mesureEPA.getValeur() != null) {
                    existingMesureEPA.setValeur(mesureEPA.getValeur());
                }
                if (mesureEPA.getDate() != null) {
                    existingMesureEPA.setDate(mesureEPA.getDate());
                }

                return existingMesureEPA;
            })
            .map(mesureEPARepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesureEPA.getId().toString())
        );
    }

    /**
     * {@code GET  /mesure-epas} : get all the mesureEPAS.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mesureEPAS in body.
     */
    @GetMapping("")
    public List<MesureEPA> getAllMesureEPAS(@RequestParam(required = false) String filter) {
        if ("alerte-is-null".equals(filter)) {
            log.debug("REST request to get all MesureEPAs where alerte is null");
            return StreamSupport
                .stream(mesureEPARepository.findAll().spliterator(), false)
                .filter(mesureEPA -> mesureEPA.getAlerte() == null)
                .toList();
        }
        log.debug("REST request to get all MesureEPAS");
        return mesureEPARepository.findAll();
    }

    /**
     * {@code GET  /mesure-epas/:id} : get the "id" mesureEPA.
     *
     * @param id the id of the mesureEPA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mesureEPA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MesureEPA> getMesureEPA(@PathVariable Long id) {
        log.debug("REST request to get MesureEPA : {}", id);
        Optional<MesureEPA> mesureEPA = mesureEPARepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mesureEPA);
    }

    /**
     * {@code DELETE  /mesure-epas/:id} : delete the "id" mesureEPA.
     *
     * @param id the id of the mesureEPA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesureEPA(@PathVariable Long id) {
        log.debug("REST request to delete MesureEPA : {}", id);
        try {
            alerteRepository.deleteById(mesureEPARepository.getReferenceById(id).getAlerte().getId());
        } catch (Exception e) {
            //Nothing to do here
        }
        mesureEPARepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
