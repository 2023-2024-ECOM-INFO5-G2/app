package polytech.ecom.g02.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static polytech.ecom.g02.domain.AlerteTestSamples.*;
import static polytech.ecom.g02.domain.EtablissementTestSamples.*;
import static polytech.ecom.g02.domain.MesureAlbumineTestSamples.*;
import static polytech.ecom.g02.domain.MesureEPATestSamples.*;
import static polytech.ecom.g02.domain.MesurePoidsTestSamples.*;
import static polytech.ecom.g02.domain.PatientTestSamples.*;
import static polytech.ecom.g02.domain.RappelTestSamples.*;
import static polytech.ecom.g02.domain.RepasTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import polytech.ecom.g02.web.rest.TestUtil;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void alerteTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Alerte alerteBack = getAlerteRandomSampleGenerator();

        patient.addAlerte(alerteBack);
        assertThat(patient.getAlertes()).containsOnly(alerteBack);
        assertThat(alerteBack.getPatient()).isEqualTo(patient);

        patient.removeAlerte(alerteBack);
        assertThat(patient.getAlertes()).doesNotContain(alerteBack);
        assertThat(alerteBack.getPatient()).isNull();

        patient.alertes(new HashSet<>(Set.of(alerteBack)));
        assertThat(patient.getAlertes()).containsOnly(alerteBack);
        assertThat(alerteBack.getPatient()).isEqualTo(patient);

        patient.setAlertes(new HashSet<>());
        assertThat(patient.getAlertes()).doesNotContain(alerteBack);
        assertThat(alerteBack.getPatient()).isNull();
    }

    @Test
    void rappelTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Rappel rappelBack = getRappelRandomSampleGenerator();

        patient.addRappel(rappelBack);
        assertThat(patient.getRappels()).containsOnly(rappelBack);
        assertThat(rappelBack.getPatient()).isEqualTo(patient);

        patient.removeRappel(rappelBack);
        assertThat(patient.getRappels()).doesNotContain(rappelBack);
        assertThat(rappelBack.getPatient()).isNull();

        patient.rappels(new HashSet<>(Set.of(rappelBack)));
        assertThat(patient.getRappels()).containsOnly(rappelBack);
        assertThat(rappelBack.getPatient()).isEqualTo(patient);

        patient.setRappels(new HashSet<>());
        assertThat(patient.getRappels()).doesNotContain(rappelBack);
        assertThat(rappelBack.getPatient()).isNull();
    }

    @Test
    void mesurePoidsTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        MesurePoids mesurePoidsBack = getMesurePoidsRandomSampleGenerator();

        patient.addMesurePoids(mesurePoidsBack);
        assertThat(patient.getMesurePoids()).containsOnly(mesurePoidsBack);
        assertThat(mesurePoidsBack.getPatient()).isEqualTo(patient);

        patient.removeMesurePoids(mesurePoidsBack);
        assertThat(patient.getMesurePoids()).doesNotContain(mesurePoidsBack);
        assertThat(mesurePoidsBack.getPatient()).isNull();

        patient.mesurePoids(new HashSet<>(Set.of(mesurePoidsBack)));
        assertThat(patient.getMesurePoids()).containsOnly(mesurePoidsBack);
        assertThat(mesurePoidsBack.getPatient()).isEqualTo(patient);

        patient.setMesurePoids(new HashSet<>());
        assertThat(patient.getMesurePoids()).doesNotContain(mesurePoidsBack);
        assertThat(mesurePoidsBack.getPatient()).isNull();
    }

    @Test
    void mesureEPATest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        MesureEPA mesureEPABack = getMesureEPARandomSampleGenerator();

        patient.addMesureEPA(mesureEPABack);
        assertThat(patient.getMesureEPAS()).containsOnly(mesureEPABack);
        assertThat(mesureEPABack.getPatient()).isEqualTo(patient);

        patient.removeMesureEPA(mesureEPABack);
        assertThat(patient.getMesureEPAS()).doesNotContain(mesureEPABack);
        assertThat(mesureEPABack.getPatient()).isNull();

        patient.mesureEPAS(new HashSet<>(Set.of(mesureEPABack)));
        assertThat(patient.getMesureEPAS()).containsOnly(mesureEPABack);
        assertThat(mesureEPABack.getPatient()).isEqualTo(patient);

        patient.setMesureEPAS(new HashSet<>());
        assertThat(patient.getMesureEPAS()).doesNotContain(mesureEPABack);
        assertThat(mesureEPABack.getPatient()).isNull();
    }

    @Test
    void mesureAlbumineTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        MesureAlbumine mesureAlbumineBack = getMesureAlbumineRandomSampleGenerator();

        patient.addMesureAlbumine(mesureAlbumineBack);
        assertThat(patient.getMesureAlbumines()).containsOnly(mesureAlbumineBack);
        assertThat(mesureAlbumineBack.getPatient()).isEqualTo(patient);

        patient.removeMesureAlbumine(mesureAlbumineBack);
        assertThat(patient.getMesureAlbumines()).doesNotContain(mesureAlbumineBack);
        assertThat(mesureAlbumineBack.getPatient()).isNull();

        patient.mesureAlbumines(new HashSet<>(Set.of(mesureAlbumineBack)));
        assertThat(patient.getMesureAlbumines()).containsOnly(mesureAlbumineBack);
        assertThat(mesureAlbumineBack.getPatient()).isEqualTo(patient);

        patient.setMesureAlbumines(new HashSet<>());
        assertThat(patient.getMesureAlbumines()).doesNotContain(mesureAlbumineBack);
        assertThat(mesureAlbumineBack.getPatient()).isNull();
    }

    @Test
    void repasTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Repas repasBack = getRepasRandomSampleGenerator();

        patient.addRepas(repasBack);
        assertThat(patient.getRepas()).containsOnly(repasBack);
        assertThat(repasBack.getPatient()).isEqualTo(patient);

        patient.removeRepas(repasBack);
        assertThat(patient.getRepas()).doesNotContain(repasBack);
        assertThat(repasBack.getPatient()).isNull();

        patient.repas(new HashSet<>(Set.of(repasBack)));
        assertThat(patient.getRepas()).containsOnly(repasBack);
        assertThat(repasBack.getPatient()).isEqualTo(patient);

        patient.setRepas(new HashSet<>());
        assertThat(patient.getRepas()).doesNotContain(repasBack);
        assertThat(repasBack.getPatient()).isNull();
    }

    @Test
    void etablissementTest() throws Exception {
        Patient patient = getPatientRandomSampleGenerator();
        Etablissement etablissementBack = getEtablissementRandomSampleGenerator();

        patient.setEtablissement(etablissementBack);
        assertThat(patient.getEtablissement()).isEqualTo(etablissementBack);

        patient.etablissement(null);
        assertThat(patient.getEtablissement()).isNull();
    }
}
