package polytech.g02.ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.g02.ecom.domain.MedecinTestSamples.*;
import static polytech.g02.ecom.domain.PatientTestSamples.*;
import static polytech.g02.ecom.domain.RappelTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.g02.ecom.web.rest.TestUtil;

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

    @Test
    void medecinTest() throws Exception {
        Rappel rappel = getRappelRandomSampleGenerator();
        Medecin medecinBack = getMedecinRandomSampleGenerator();

        rappel.addMedecin(medecinBack);
        assertThat(rappel.getMedecins()).containsOnly(medecinBack);

        rappel.removeMedecin(medecinBack);
        assertThat(rappel.getMedecins()).doesNotContain(medecinBack);

        rappel.medecins(new HashSet<>(Set.of(medecinBack)));
        assertThat(rappel.getMedecins()).containsOnly(medecinBack);

        rappel.setMedecins(new HashSet<>());
        assertThat(rappel.getMedecins()).doesNotContain(medecinBack);
    }
}
