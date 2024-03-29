package polytech.ecom.g02.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import polytech.ecom.g02.domain.Alerte;
import polytech.ecom.g02.domain.MesurePoids;
import polytech.ecom.g02.domain.Patient;
import polytech.ecom.g02.repository.AlerteRepository;
import polytech.ecom.g02.repository.MesurePoidsRepository;
import polytech.ecom.g02.repository.PatientRepository;
import polytech.ecom.g02.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link polytech.ecom.g02.domain.MesurePoids}.
 */
@RestController
@RequestMapping("/api/mesure-poids")
@Transactional
public class MesurePoidsResource {

    private final Logger log = LoggerFactory.getLogger(MesurePoidsResource.class);

    private static final String ENTITY_NAME = "mesurePoids";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MesurePoidsRepository mesurePoidsRepository;
    private final AlerteRepository alerteRepository;
    private final PatientRepository patientRepository;

    public MesurePoidsResource(
        MesurePoidsRepository mesurePoidsRepository,
        AlerteRepository alerteRepository,
        PatientRepository patientRepository
    ) {
        this.mesurePoidsRepository = mesurePoidsRepository;
        this.alerteRepository = alerteRepository;
        this.patientRepository = patientRepository;
    }

    private boolean isNewest(Patient patient, ZonedDateTime date) {
        Set<MesurePoids> mesures = patient.getMesurePoids();

        for (MesurePoids mesure : mesures) {
            if (mesure.getDate().isAfter(date)) return false;
        }
        return true;
    }

    private void check(MesurePoids mesurePoids) {
        if (mesurePoids.getPatient() == null) return; //Should not happen naturally but exist in tests
        Patient patient = patientRepository.getReferenceById(mesurePoids.getPatient().getId()).addMesurePoids(mesurePoids);
        if (!isNewest(patient, mesurePoids.getDate())) return;

        List<Alerte> alertes = alerteRepository.findAll();
        if (alertes != null) {
            for (Alerte alerte : alertes) {
                if (alerte.getMesurePoids() != null) {
                    alerteRepository.delete(alerte);
                    patient.removeAlerte(alerte);
                }
            }
        }
        double IMC = (double) Math.round((mesurePoids.getValeur() / Math.pow(mesurePoids.getPatient().getTaille() / 100, 2)) * 100) / 100;
        ZonedDateTime currentDate = mesurePoids.getDate();
        if (IMC < 18.5) {
            Alerte alerte = new Alerte();
            alerte.setPatient(patient);
            alerte.setDate(currentDate);
            alerte.setMesurePoids(mesurePoids);
            if (IMC < 17) {
                alerte.setSevere(true);
                alerte.setCode(21);
                alerte.setDescription("Alerte IMC très faible : " + IMC);
            } else {
                alerte.setSevere(false);
                alerte.setCode(20);
                alerte.setDescription("Attention IMC faible : " + IMC);
            }
            patient.addAlerte(alerte);
            alerteRepository.save(alerte);
        }

        List<MesurePoids> poidsList = mesurePoidsRepository.findAll();
        poidsList.removeIf(poids -> poids.getPatient() == null);
        poidsList.removeIf(poids -> !poids.getPatient().getId().equals(patient.getId()));
        MesurePoids first = null;

        ZonedDateTime startDate1 = currentDate.minusMonths(1).minusDays(3);
        ZonedDateTime endDate1 = currentDate.minusMonths(1).plusDays(3);

        ZonedDateTime startDate6 = currentDate.minusMonths(6).minusDays(3);
        ZonedDateTime endDate6 = currentDate.minusMonths(6).plusDays(3);

        List<MesurePoids> filteredPoids1 = new ArrayList<>();
        List<MesurePoids> filteredPoids6 = new ArrayList<>();

        for (MesurePoids poids : poidsList) {
            ZonedDateTime date = poids.getDate();
            if (first == null) first = poids;
            if (first.getDate().isAfter(date)) first = poids;
            if (date.isAfter(startDate1) && date.isBefore(endDate1)) {
                filteredPoids1.add(poids);
            }
            if (date.isAfter(startDate6) && date.isBefore(endDate6)) {
                filteredPoids6.add(poids);
            }
        }

        if (!filteredPoids1.isEmpty()) {
            float ratio1 =
                mesurePoids.getValeur() / Collections.min(filteredPoids1, Comparator.comparing(MesurePoids::getValeur)).getValeur();
            if (ratio1 < 0.95) {
                Alerte alerte = new Alerte();
                alerte.setMesurePoids(mesurePoids);
                alerte.setPatient(mesurePoids.getPatient());
                alerte.setDate(currentDate);
                if (ratio1 < 0.9) {
                    alerte.setSevere(true);
                    alerte.setCode(11);
                    alerte.setDescription(
                        "Alerte Perte de poids très rapide : " + Math.round((100 - ratio1 * 100) * 100) / 100 + "% en 1 mois"
                    );
                } else {
                    alerte.setSevere(false);
                    alerte.setCode(10);
                    alerte.setDescription(
                        "Attention Perte de poids rapide : " + Math.round((100 - ratio1 * 100) * 100) / 100 + "% en 1 mois"
                    );
                }
                patient.addAlerte(alerte);
                alerteRepository.save(alerte);
            }
        }

        if (!filteredPoids6.isEmpty()) {
            float ratio6 =
                mesurePoids.getValeur() / Collections.min(filteredPoids6, Comparator.comparing(MesurePoids::getValeur)).getValeur(); //.get(filteredPoids6.size() / 2).getValeur();
            if (ratio6 < 0.9) {
                Alerte alerte = new Alerte();
                alerte.setMesurePoids(mesurePoids);
                alerte.setPatient(mesurePoids.getPatient());
                alerte.setDate(currentDate);
                if (ratio6 < 0.85) {
                    alerte.setSevere(true);
                    alerte.setCode(13);
                    alerte.setDescription(
                        "Alerte Perte de poids très rapide : " + Math.round((100 - ratio6 * 100) * 100) / 100 + "% en 6 mois"
                    );
                } else {
                    alerte.setSevere(false);
                    alerte.setCode(12);
                    alerte.setDescription(
                        "Attention Perte de poids rapide : " + Math.round((100 - ratio6 * 100) * 100) / 100 + "% en 6 mois"
                    );
                }
                patient.addAlerte(alerte);
                alerteRepository.save(alerte);
            }
        }

        if (first != null && !filteredPoids1.contains(first) && !filteredPoids6.contains(first)) {
            float ratio = mesurePoids.getValeur() / first.getValeur();
            if (ratio < 0.9) {
                Alerte alerte = new Alerte();
                alerte.setMesurePoids(mesurePoids);
                alerte.setPatient(mesurePoids.getPatient());
                alerte.setDate(currentDate);
                if (ratio < 0.85) {
                    alerte.setSevere(true);
                    alerte.setCode(15);
                    alerte.setDescription(
                        "Alerte Perte de poids très importante : " +
                        Math.round((100 - ratio * 100) * 100) / 100 +
                        "% depuis la première prise de poids"
                    );
                } else {
                    alerte.setSevere(false);
                    alerte.setCode(14);
                    alerte.setDescription(
                        "Attention Perte de poids importante : " +
                        Math.round((100 - ratio * 100) * 100) / 100 +
                        "% depuis la première prise de poids"
                    );
                }
                patient.addAlerte(alerte);
                alerteRepository.save(alerte);
            }
        }
        patientRepository.save(patient);
    }

    /**
     * {@code POST  /mesure-poids} : Create a new mesurePoids.
     *
     * @param mesurePoids the mesurePoids to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mesurePoids, or with status {@code 400 (Bad Request)} if the mesurePoids has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MesurePoids> createMesurePoids(@Valid @RequestBody MesurePoids mesurePoids) throws URISyntaxException {
        log.debug("REST request to save MesurePoids : {}", mesurePoids);
        if (mesurePoids.getId() != null) {
            throw new BadRequestAlertException("A new mesurePoids cannot already have an ID", ENTITY_NAME, "idexists");
        }
        System.out.println(" --------------------------------------------------------------------------------");
        System.out.println(mesurePoids.getId() + " ------------- ");

        MesurePoids result = mesurePoidsRepository.save(mesurePoids);
        System.out.println(mesurePoids.getId() + " ------------- ");
        check(mesurePoids);
        System.out.println(mesurePoids.getId() + " ------------- " + result.getId());
        System.out.println(" --------------------------------------------------------------------------------");

        return ResponseEntity
            .created(new URI("/api/mesure-poids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mesure-poids/:id} : Updates an existing mesurePoids.
     *
     * @param id the id of the mesurePoids to save.
     * @param mesurePoids the mesurePoids to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesurePoids,
     * or with status {@code 400 (Bad Request)} if the mesurePoids is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mesurePoids couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MesurePoids> updateMesurePoids(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MesurePoids mesurePoids
    ) throws URISyntaxException {
        log.debug("REST request to update MesurePoids : {}, {}", id, mesurePoids);
        if (mesurePoids.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesurePoids.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesurePoidsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MesurePoids result = mesurePoidsRepository.save(mesurePoids);
        check(mesurePoids);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesurePoids.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mesure-poids/:id} : Partial updates given fields of an existing mesurePoids, field will ignore if it is null
     *
     * @param id the id of the mesurePoids to save.
     * @param mesurePoids the mesurePoids to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesurePoids,
     * or with status {@code 400 (Bad Request)} if the mesurePoids is not valid,
     * or with status {@code 404 (Not Found)} if the mesurePoids is not found,
     * or with status {@code 500 (Internal Server Error)} if the mesurePoids couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MesurePoids> partialUpdateMesurePoids(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MesurePoids mesurePoids
    ) throws URISyntaxException {
        log.debug("REST request to partial update MesurePoids partially : {}, {}", id, mesurePoids);
        if (mesurePoids.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesurePoids.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesurePoidsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MesurePoids> result = mesurePoidsRepository
            .findById(mesurePoids.getId())
            .map(existingMesurePoids -> {
                if (mesurePoids.getValeur() != null) {
                    existingMesurePoids.setValeur(mesurePoids.getValeur());
                }
                if (mesurePoids.getDate() != null) {
                    existingMesurePoids.setDate(mesurePoids.getDate());
                }

                return existingMesurePoids;
            })
            .map(mesurePoidsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesurePoids.getId().toString())
        );
    }

    /**
     * {@code GET  /mesure-poids} : get all the mesurePoids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mesurePoids in body.
     */
    @GetMapping("")
    public List<MesurePoids> getAllMesurePoids() {
        log.debug("REST request to get all MesurePoids");
        return mesurePoidsRepository.findAll();
    }

    /**
     * {@code GET  /mesure-poids/:id} : get the "id" mesurePoids.
     *
     * @param id the id of the mesurePoids to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mesurePoids, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MesurePoids> getMesurePoids(@PathVariable Long id) {
        log.debug("REST request to get MesurePoids : {}", id);
        Optional<MesurePoids> mesurePoids = mesurePoidsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mesurePoids);
    }

    /**
     * {@code DELETE  /mesure-poids/:id} : delete the "id" mesurePoids.
     *
     * @param id the id of the mesurePoids to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesurePoids(@PathVariable Long id) {
        log.debug("REST request to delete MesurePoids : {}", id);
        try {
            for (Alerte alerte : mesurePoidsRepository.getReferenceById(id).getAlertes()) {
                alerteRepository.deleteById(alerte.getId());
            }
        } catch (Exception e) {
            // Nothing to do here
        }
        mesurePoidsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
