package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.AideSoignantTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

class AideSoignantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AideSoignant.class);
        AideSoignant aideSoignant1 = getAideSoignantSample1();
        AideSoignant aideSoignant2 = new AideSoignant();
        assertThat(aideSoignant1).isNotEqualTo(aideSoignant2);

        aideSoignant2.setId(aideSoignant1.getId());
        assertThat(aideSoignant1).isEqualTo(aideSoignant2);

        aideSoignant2 = getAideSoignantSample2();
        assertThat(aideSoignant1).isNotEqualTo(aideSoignant2);
    }

    @Test
    void patientTest() throws Exception {
        AideSoignant aideSoignant = getAideSoignantRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        aideSoignant.addPatient(patientBack);
        assertThat(aideSoignant.getPatients()).containsOnly(patientBack);

        aideSoignant.removePatient(patientBack);
        assertThat(aideSoignant.getPatients()).doesNotContain(patientBack);

        aideSoignant.patients(new HashSet<>(Set.of(patientBack)));
        assertThat(aideSoignant.getPatients()).containsOnly(patientBack);

        aideSoignant.setPatients(new HashSet<>());
        assertThat(aideSoignant.getPatients()).doesNotContain(patientBack);
    }
}
