package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.MesurePoidsTestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class MesurePoidsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MesurePoids.class);
        MesurePoids mesurePoids1 = getMesurePoidsSample1();
        MesurePoids mesurePoids2 = new MesurePoids();
        assertThat(mesurePoids1).isNotEqualTo(mesurePoids2);

        mesurePoids2.setId(mesurePoids1.getId());
        assertThat(mesurePoids1).isEqualTo(mesurePoids2);

        mesurePoids2 = getMesurePoidsSample2();
        assertThat(mesurePoids1).isNotEqualTo(mesurePoids2);
    }

    @Test
    void patientTest() throws Exception {
        MesurePoids mesurePoids = getMesurePoidsRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        mesurePoids.setPatient(patientBack);
        assertThat(mesurePoids.getPatient()).isEqualTo(patientBack);

        mesurePoids.patient(null);
        assertThat(mesurePoids.getPatient()).isNull();
    }
}
