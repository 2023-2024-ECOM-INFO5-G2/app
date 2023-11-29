package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.AlerteTestSamples.*;
import static polytech.g02.ecom.domain.MedecinTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class AlerteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alerte.class);
        Alerte alerte1 = getAlerteSample1();
        Alerte alerte2 = new Alerte();
        assertThat(alerte1).isNotEqualTo(alerte2);

        alerte2.setId(alerte1.getId());
        assertThat(alerte1).isEqualTo(alerte2);

        alerte2 = getAlerteSample2();
        assertThat(alerte1).isNotEqualTo(alerte2);
    }

    @Test
    void patientTest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        alerte.addPatient(patientBack);
        assertThat(alerte.getPatients()).containsOnly(patientBack);

        alerte.removePatient(patientBack);
        assertThat(alerte.getPatients()).doesNotContain(patientBack);

        alerte.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(alerte.getPatients()).containsOnly(patientBack);

        alerte.setPatients(new HashSet<>());
        assertThat(alerte.getPatients()).doesNotContain(patientBack);
    }

    @Test
    void medecinTest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        Medecin medecinBack = getMedecinRandomSampleGenerator();

        alerte.addMedecin(medecinBack);
        assertThat(alerte.getMedecins()).containsOnly(medecinBack);
        assertThat(medecinBack.getAlertes()).containsOnly(alerte);

        alerte.removeMedecin(medecinBack);
        assertThat(alerte.getMedecins()).doesNotContain(medecinBack);
        assertThat(medecinBack.getAlertes()).doesNotContain(alerte);

        alerte.medecins(new HashSet<>(Set.of(medecinBack)));
        assertThat(alerte.getMedecins()).containsOnly(medecinBack);
        assertThat(medecinBack.getAlertes()).containsOnly(alerte);

        alerte.setMedecins(new HashSet<>());
        assertThat(alerte.getMedecins()).doesNotContain(medecinBack);
        assertThat(medecinBack.getAlertes()).doesNotContain(alerte);
    }
}
