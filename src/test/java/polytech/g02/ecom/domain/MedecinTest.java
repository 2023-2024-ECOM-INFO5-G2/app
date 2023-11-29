package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.AlerteTestSamples.*;
import static polytech.g02.ecom.domain.MedecinTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;
import static polytech.g02.ecom.domain.RappelTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class MedecinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medecin.class);
        Medecin medecin1 = getMedecinSample1();
        Medecin medecin2 = new Medecin();
        assertThat(medecin1).isNotEqualTo(medecin2);

        medecin2.setId(medecin1.getId());
        assertThat(medecin1).isEqualTo(medecin2);

        medecin2 = getMedecinSample2();
        assertThat(medecin1).isNotEqualTo(medecin2);
    }

    @Test
    void patientTest() throws Exception {
        Medecin medecin = getMedecinRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        medecin.addPatient(patientBack);
        assertThat(medecin.getPatients()).containsOnly(patientBack);
        assertThat(patientBack.getMedecin()).isEqualTo(medecin);

        medecin.removePatient(patientBack);
        assertThat(medecin.getPatients()).doesNotContain(patientBack);
        assertThat(patientBack.getMedecin()).isNull();

        medecin.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(medecin.getPatients()).containsOnly(patientBack);
        assertThat(patientBack.getMedecin()).isEqualTo(medecin);

        medecin.setPatients(new HashSet<>());
        assertThat(medecin.getPatients()).doesNotContain(patientBack);
        assertThat(patientBack.getMedecin()).isNull();
    }

    @Test
    void alerteTest() throws Exception {
        Medecin medecin = getMedecinRandomSampleGenerator();
        Alerte alerteBack = getAlerteRandomSampleGenerator();

        medecin.addAlerte(alerteBack);
        assertThat(medecin.getAlertes()).containsOnly(alerteBack);

        medecin.removeAlerte(alerteBack);
        assertThat(medecin.getAlertes()).doesNotContain(alerteBack);

        medecin.alertes(new HashSet<>(Set.of(alerteBack)));
        assertThat(medecin.getAlertes()).containsOnly(alerteBack);

        medecin.setAlertes(new HashSet<>());
        assertThat(medecin.getAlertes()).doesNotContain(alerteBack);
    }

    @Test
    void rappelTest() throws Exception {
        Medecin medecin = getMedecinRandomSampleGenerator();
        Rappel rappelBack = getRappelRandomSampleGenerator();

        medecin.addRappel(rappelBack);
        assertThat(medecin.getRappels()).containsOnly(rappelBack);
        assertThat(rappelBack.getMedecins()).containsOnly(medecin);

        medecin.removeRappel(rappelBack);
        assertThat(medecin.getRappels()).doesNotContain(rappelBack);
        assertThat(rappelBack.getMedecins()).doesNotContain(medecin);

        medecin.rappels(new HashSet<>(Set.of(rappelBack)));
        assertThat(medecin.getRappels()).containsOnly(rappelBack);
        assertThat(rappelBack.getMedecins()).containsOnly(medecin);

        medecin.setRappels(new HashSet<>());
        assertThat(medecin.getRappels()).doesNotContain(rappelBack);
        assertThat(rappelBack.getMedecins()).doesNotContain(medecin);
    }
}
