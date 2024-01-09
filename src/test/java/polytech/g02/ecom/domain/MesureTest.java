package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.MesureTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class MesureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mesure.class);
        Mesure mesure1 = getMesureSample1();
        Mesure mesure2 = new Mesure();
        assertThat(mesure1).isNotEqualTo(mesure2);

        mesure2.setId(mesure1.getId());
        assertThat(mesure1).isEqualTo(mesure2);

        mesure2 = getMesureSample2();
        assertThat(mesure1).isNotEqualTo(mesure2);
    }

    @Test
    void patientTest() throws Exception {
        Mesure mesure = getMesureRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        mesure.setPatient(patientBack);
        assertThat(mesure.getPatient()).isEqualTo(patientBack);

        mesure.patient(null);
        assertThat(mesure.getPatient()).isNull();
    }
}
