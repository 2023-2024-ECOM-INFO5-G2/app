package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.InfirmiereTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class InfirmiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Infirmiere.class);
        Infirmiere infirmiere1 = getInfirmiereSample1();
        Infirmiere infirmiere2 = new Infirmiere();
        assertThat(infirmiere1).isNotEqualTo(infirmiere2);

        infirmiere2.setId(infirmiere1.getId());
        assertThat(infirmiere1).isEqualTo(infirmiere2);

        infirmiere2 = getInfirmiereSample2();
        assertThat(infirmiere1).isNotEqualTo(infirmiere2);
    }

    @Test
    void patientTest() throws Exception {
        Infirmiere infirmiere = getInfirmiereRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        infirmiere.addPatient(patientBack);
        assertThat(infirmiere.getPatients()).containsOnly(patientBack);

        infirmiere.removePatient(patientBack);
        assertThat(infirmiere.getPatients()).doesNotContain(patientBack);

        infirmiere.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(infirmiere.getPatients()).containsOnly(patientBack);

        infirmiere.setPatients(new HashSet<>());
        assertThat(infirmiere.getPatients()).doesNotContain(patientBack);
    }
}
