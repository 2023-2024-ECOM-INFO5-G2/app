package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.MesureEPATestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class MesureEPATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MesureEPA.class);
        MesureEPA mesureEPA1 = getMesureEPASample1();
        MesureEPA mesureEPA2 = new MesureEPA();
        assertThat(mesureEPA1).isNotEqualTo(mesureEPA2);

        mesureEPA2.setId(mesureEPA1.getId());
        assertThat(mesureEPA1).isEqualTo(mesureEPA2);

        mesureEPA2 = getMesureEPASample2();
        assertThat(mesureEPA1).isNotEqualTo(mesureEPA2);
    }

    @Test
    void patientTest() throws Exception {
        MesureEPA mesureEPA = getMesureEPARandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        mesureEPA.setPatient(patientBack);
        assertThat(mesureEPA.getPatient()).isEqualTo(patientBack);

        mesureEPA.patient(null);
        assertThat(mesureEPA.getPatient()).isNull();
    }
}
