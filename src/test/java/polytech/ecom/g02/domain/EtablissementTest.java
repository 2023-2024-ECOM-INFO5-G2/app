package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.EtablissementTestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class EtablissementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etablissement.class);
        Etablissement etablissement1 = getEtablissementSample1();
        Etablissement etablissement2 = new Etablissement();
        assertThat(etablissement1).isNotEqualTo(etablissement2);

        etablissement2.setId(etablissement1.getId());
        assertThat(etablissement1).isEqualTo(etablissement2);

        etablissement2 = getEtablissementSample2();
        assertThat(etablissement1).isNotEqualTo(etablissement2);
    }

    @Test
    void patientTest() throws Exception {
        Etablissement etablissement = getEtablissementRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        etablissement.addPatient(patientBack);
        assertThat(etablissement.getPatients()).containsOnly(patientBack);
        assertThat(patientBack.getEtablissement()).isEqualTo(etablissement);

        etablissement.removePatient(patientBack);
        assertThat(etablissement.getPatients()).doesNotContain(patientBack);
        assertThat(patientBack.getEtablissement()).isNull();

        etablissement.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(etablissement.getPatients()).containsOnly(patientBack);
        assertThat(patientBack.getEtablissement()).isEqualTo(etablissement);

        etablissement.setPatients(new HashSet<>());
        assertThat(etablissement.getPatients()).doesNotContain(patientBack);
        assertThat(patientBack.getEtablissement()).isNull();
    }
}
