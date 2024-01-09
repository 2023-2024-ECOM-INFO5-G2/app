package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.PatientTestSamples.*;
import static polytech.g02.ecom.domain.RepasTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class RepasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repas.class);
        Repas repas1 = getRepasSample1();
        Repas repas2 = new Repas();
        assertThat(repas1).isNotEqualTo(repas2);

        repas2.setId(repas1.getId());
        assertThat(repas1).isEqualTo(repas2);

        repas2 = getRepasSample2();
        assertThat(repas1).isNotEqualTo(repas2);
    }

    @Test
    void patientTest() throws Exception {
        Repas repas = getRepasRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        repas.addPatient(patientBack);
        assertThat(repas.getPatients()).containsOnly(patientBack);

        repas.removePatient(patientBack);
        assertThat(repas.getPatients()).doesNotContain(patientBack);

        repas.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(repas.getPatients()).containsOnly(patientBack);

        repas.setPatients(new HashSet<>());
        assertThat(repas.getPatients()).doesNotContain(patientBack);
    }
}
