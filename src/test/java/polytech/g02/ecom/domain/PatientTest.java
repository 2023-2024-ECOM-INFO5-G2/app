package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.AideSoignantTestSamples.*;
import static polytech.g02.ecom.domain.AlerteTestSamples.*;
import static polytech.g02.ecom.domain.EtablissementTestSamples.*;
import static polytech.g02.ecom.domain.InfirmiereTestSamples.*;
import static polytech.g02.ecom.domain.MedecinTestSamples.*;
import static polytech.g02.ecom.domain.MesureTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;
import static polytech.g02.ecom.domain.RappelTestSamples.*;
import static polytech.g02.ecom.domain.RepasTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void mesureTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Mesure mesureBack = getMesureRandomSampleGenerator();

        patient.addMesure(mesureBack);
        assertThat(patient.getMesures()).containsOnly(mesureBack);
        assertThat(mesureBack.getPatient()).isEqualTo(patient);

        patient.removeMesure(mesureBack);
        assertThat(patient.getMesures()).doesNotContain(mesureBack);
        assertThat(mesureBack.getPatient()).isNull();

        patient.mesures(new HashSet<>(Set.of(mesureBack)));
        assertThat(patient.getMesures()).containsOnly(mesureBack);
        assertThat(mesureBack.getPatient()).isEqualTo(patient);

        patient.setMesures(new HashSet<>());
        assertThat(patient.getMesures()).doesNotContain(mesureBack);
        assertThat(mesureBack.getPatient()).isNull();
    }

    @Test
    void rappelTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Rappel rappelBack = getRappelRandomSampleGenerator();

        patient.setRappel(rappelBack);
        assertThat(patient.getRappel()).isEqualTo(rappelBack);
        assertThat(rappelBack.getPatient()).isEqualTo(patient);

        patient.rappel(null);
        assertThat(patient.getRappel()).isNull();
        assertThat(rappelBack.getPatient()).isNull();
    }

    @Test
    void medecinTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Medecin medecinBack = getMedecinRandomSampleGenerator();

        patient.setMedecin(medecinBack);
        assertThat(patient.getMedecin()).isEqualTo(medecinBack);

        patient.medecin(null);
        assertThat(patient.getMedecin()).isNull();
    }

    @Test
    void etablissementTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Etablissement etablissementBack = getEtablissementRandomSampleGenerator();

        patient.setEtablissement(etablissementBack);
        assertThat(patient.getEtablissement()).isEqualTo(etablissementBack);

        patient.etablissement(null);
        assertThat(patient.getEtablissement()).isNull();
    }

    @Test
    void aideSoignantTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        AideSoignant aideSoignantBack = getAideSoignantRandomSampleGenerator();

        patient.addAideSoignant(aideSoignantBack);
        assertThat(patient.getAideSoignants()).containsOnly(aideSoignantBack);
        assertThat(aideSoignantBack.getPatients()).containsOnly(patient);

        patient.removeAideSoignant(aideSoignantBack);
        assertThat(patient.getAideSoignants()).doesNotContain(aideSoignantBack);
        assertThat(aideSoignantBack.getPatients()).doesNotContain(patient);

        patient.aideSoignants(new HashSet<>(Set.of(aideSoignantBack)));
        assertThat(patient.getAideSoignants()).containsOnly(aideSoignantBack);
        assertThat(aideSoignantBack.getPatients()).containsOnly(patient);

        patient.setAideSoignants(new HashSet<>());
        assertThat(patient.getAideSoignants()).doesNotContain(aideSoignantBack);
        assertThat(aideSoignantBack.getPatients()).doesNotContain(patient);
    }

    @Test
    void infirmiereTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Infirmiere infirmiereBack = getInfirmiereRandomSampleGenerator();

        patient.addInfirmiere(infirmiereBack);
        assertThat(patient.getInfirmieres()).containsOnly(infirmiereBack);
        assertThat(infirmiereBack.getPatients()).containsOnly(patient);

        patient.removeInfirmiere(infirmiereBack);
        assertThat(patient.getInfirmieres()).doesNotContain(infirmiereBack);
        assertThat(infirmiereBack.getPatients()).doesNotContain(patient);

        patient.infirmieres(new HashSet<>(Set.of(infirmiereBack)));
        assertThat(patient.getInfirmieres()).containsOnly(infirmiereBack);
        assertThat(infirmiereBack.getPatients()).containsOnly(patient);

        patient.setInfirmieres(new HashSet<>());
        assertThat(patient.getInfirmieres()).doesNotContain(infirmiereBack);
        assertThat(infirmiereBack.getPatients()).doesNotContain(patient);
    }

    @Test
    void repasTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Repas repasBack = getRepasRandomSampleGenerator();

        patient.addRepas(repasBack);
        assertThat(patient.getRepas()).containsOnly(repasBack);
        assertThat(repasBack.getPatients()).containsOnly(patient);

        patient.removeRepas(repasBack);
        assertThat(patient.getRepas()).doesNotContain(repasBack);
        assertThat(repasBack.getPatients()).doesNotContain(patient);

        patient.repas(new HashSet<>(Set.of(repasBack)));
        assertThat(patient.getRepas()).containsOnly(repasBack);
        assertThat(repasBack.getPatients()).containsOnly(patient);

        patient.setRepas(new HashSet<>());
        assertThat(patient.getRepas()).doesNotContain(repasBack);
        assertThat(repasBack.getPatients()).doesNotContain(patient);
    }

    @Test
    void alerteTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Alerte alerteBack = getAlerteRandomSampleGenerator();

        patient.addAlerte(alerteBack);
        assertThat(patient.getAlertes()).containsOnly(alerteBack);
        assertThat(alerteBack.getPatients()).containsOnly(patient);

        patient.removeAlerte(alerteBack);
        assertThat(patient.getAlertes()).doesNotContain(alerteBack);
        assertThat(alerteBack.getPatients()).doesNotContain(patient);

        patient.alertes(new HashSet<>(Set.of(alerteBack)));
        assertThat(patient.getAlertes()).containsOnly(alerteBack);
        assertThat(alerteBack.getPatients()).containsOnly(patient);

        patient.setAlertes(new HashSet<>());
        assertThat(patient.getAlertes()).doesNotContain(alerteBack);
        assertThat(alerteBack.getPatients()).doesNotContain(patient);
    }
}
