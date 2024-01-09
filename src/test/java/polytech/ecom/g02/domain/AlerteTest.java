package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.AlerteTestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

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

        alerte.setPatient(patientBack);
        assertThat(alerte.getPatient()).isEqualTo(patientBack);

        alerte.patient(null);
        assertThat(alerte.getPatient()).isNull();
    }
}
