package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.PatientTestSamples.*;
import static polytech.ecom.g02.domain.RepasTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

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

        repas.setPatient(patientBack);
        assertThat(repas.getPatient()).isEqualTo(patientBack);

        repas.patient(null);
        assertThat(repas.getPatient()).isNull();
    }
}
