package polytech.g02.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Repas.
 */
@Entity
@Table(name = "repas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Repas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "apport_calorique", nullable = false)
    private Integer apportCalorique;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_repas__patient",
        joinColumns = @JoinColumn(name = "repas_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "mesures", "rappel", "medecin", "etablissement", "aideSoignants", "infirmieres", "repas", "alertes" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Repas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Repas nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Repas description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getApportCalorique() {
        return this.apportCalorique;
    }

    public Repas apportCalorique(Integer apportCalorique) {
        this.setApportCalorique(apportCalorique);
        return this;
    }

    public void setApportCalorique(Integer apportCalorique) {
        this.apportCalorique = apportCalorique;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Repas patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Repas addPatient(Patient patient) {
        this.patients.add(patient);
        return this;
    }

    public Repas removePatient(Patient patient) {
        this.patients.remove(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repas)) {
            return false;
        }
        return getId() != null && getId().equals(((Repas) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repas{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", apportCalorique=" + getApportCalorique() +
            "}";
    }
}
