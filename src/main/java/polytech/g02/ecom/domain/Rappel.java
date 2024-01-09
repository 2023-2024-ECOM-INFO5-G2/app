package polytech.g02.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rappel.
 */
@Entity
@Table(name = "rappel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rappel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "frequence", nullable = false)
    private String frequence;

    @NotNull
    @Column(name = "echeance", nullable = false)
    private LocalDate echeance;

    @NotNull
    @Column(name = "tache", nullable = false)
    private String tache;

    @JsonIgnoreProperties(
        value = { "mesures", "rappel", "medecin", "etablissement", "aideSoignants", "infirmieres", "repas", "alertes" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Patient patient;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_rappel__medecin",
        joinColumns = @JoinColumn(name = "rappel_id"),
        inverseJoinColumns = @JoinColumn(name = "medecin_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patients", "users", "alertes", "rappels" }, allowSetters = true)
    private Set<Medecin> medecins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rappel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrequence() {
        return this.frequence;
    }

    public Rappel frequence(String frequence) {
        this.setFrequence(frequence);
        return this;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public LocalDate getEcheance() {
        return this.echeance;
    }

    public Rappel echeance(LocalDate echeance) {
        this.setEcheance(echeance);
        return this;
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance = echeance;
    }

    public String getTache() {
        return this.tache;
    }

    public Rappel tache(String tache) {
        this.setTache(tache);
        return this;
    }

    public void setTache(String tache) {
        this.tache = tache;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Rappel patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Set<Medecin> getMedecins() {
        return this.medecins;
    }

    public void setMedecins(Set<Medecin> medecins) {
        this.medecins = medecins;
    }

    public Rappel medecins(Set<Medecin> medecins) {
        this.setMedecins(medecins);
        return this;
    }

    public Rappel addMedecin(Medecin medecin) {
        this.medecins.add(medecin);
        return this;
    }

    public Rappel removeMedecin(Medecin medecin) {
        this.medecins.remove(medecin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rappel)) {
            return false;
        }
        return getId() != null && getId().equals(((Rappel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rappel{" +
            "id=" + getId() +
            ", frequence='" + getFrequence() + "'" +
            ", echeance='" + getEcheance() + "'" +
            ", tache='" + getTache() + "'" +
            "}";
    }
}
