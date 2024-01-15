package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.AlerteTestSamples.*;
import static polytech.ecom.g02.domain.MesureAlbumineTestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class MesureAlbumineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MesureAlbumine.class);
        MesureAlbumine mesureAlbumine1 = getMesureAlbumineSample1();
        MesureAlbumine mesureAlbumine2 = new MesureAlbumine();
        assertThat(mesureAlbumine1).isNotEqualTo(mesureAlbumine2);

        mesureAlbumine2.setId(mesureAlbumine1.getId());
        assertThat(mesureAlbumine1).isEqualTo(mesureAlbumine2);

        mesureAlbumine2 = getMesureAlbumineSample2();
        assertThat(mesureAlbumine1).isNotEqualTo(mesureAlbumine2);
    }

    @Test
    void patientTest() throws Exception {
        MesureAlbumine mesureAlbumine = getMesureAlbumineRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        mesureAlbumine.setPatient(patientBack);
        assertThat(mesureAlbumine.getPatient()).isEqualTo(patientBack);

        mesureAlbumine.patient(null);
        assertThat(mesureAlbumine.getPatient()).isNull();
    }

    @Test
    void alerteTest() throws Exception {
        MesureAlbumine mesureAlbumine = getMesureAlbumineRandomSampleGenerator();
        Alerte alerteBack = getAlerteRandomSampleGenerator();

        mesureAlbumine.setAlerte(alerteBack);
        assertThat(mesureAlbumine.getAlerte()).isEqualTo(alerteBack);
        assertThat(alerteBack.getMesureAlbumine()).isEqualTo(mesureAlbumine);

        mesureAlbumine.alerte(null);
        assertThat(mesureAlbumine.getAlerte()).isNull();
        assertThat(alerteBack.getMesureAlbumine()).isNull();
    }
}
