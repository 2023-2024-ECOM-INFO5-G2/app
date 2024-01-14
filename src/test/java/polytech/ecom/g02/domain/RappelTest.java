package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.PatientTestSamples.*;
import static polytech.ecom.g02.domain.RappelTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class RappelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rappel.class);
        Rappel rappel1 = getRappelSample1();
        Rappel rappel2 = new Rappel();
        assertThat(rappel1).isNotEqualTo(rappel2);

        rappel2.setId(rappel1.getId());
        assertThat(rappel1).isEqualTo(rappel2);

        rappel2 = getRappelSample2();
        assertThat(rappel1).isNotEqualTo(rappel2);
    }

    @Test
    void patientTest() throws Exception {
        Rappel rappel = getRappelRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        rappel.setPatient(patientBack);
        assertThat(rappel.getPatient()).isEqualTo(patientBack);

        rappel.patient(null);
        assertThat(rappel.getPatient()).isNull();
    }
}
