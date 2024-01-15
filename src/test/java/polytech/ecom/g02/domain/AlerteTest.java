package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.AlerteTestSamples.*;
import static polytech.ecom.g02.domain.MesureAlbumineTestSamples.*;
import static polytech.ecom.g02.domain.MesureEPATestSamples.*;
import static polytech.ecom.g02.domain.MesurePoidsTestSamples.*;
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

    @Test
    void mesureEPATest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        MesureEPA mesureEPABack = getMesureEPARandomSampleGenerator();

        alerte.setMesureEPA(mesureEPABack);
        assertThat(alerte.getMesureEPA()).isEqualTo(mesureEPABack);

        alerte.mesureEPA(null);
        assertThat(alerte.getMesureEPA()).isNull();
    }

    @Test
    void mesureAlbumineTest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        MesureAlbumine mesureAlbumineBack = getMesureAlbumineRandomSampleGenerator();

        alerte.setMesureAlbumine(mesureAlbumineBack);
        assertThat(alerte.getMesureAlbumine()).isEqualTo(mesureAlbumineBack);

        alerte.mesureAlbumine(null);
        assertThat(alerte.getMesureAlbumine()).isNull();
    }

    @Test
    void mesurePoidsTest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        MesurePoids mesurePoidsBack = getMesurePoidsRandomSampleGenerator();

        alerte.setMesurePoids(mesurePoidsBack);
        assertThat(alerte.getMesurePoids()).isEqualTo(mesurePoidsBack);

        alerte.mesurePoids(null);
        assertThat(alerte.getMesurePoids()).isNull();
    }
}
