package polytech.g02.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medecin.
 */
@Entity
@Table(name = "medecin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medecin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "medecin")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "mesures", "rappel", "medecin", "etablissement", "aideSoignants", "infirmieres", "repas", "alertes" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_medecin__user",
        joinColumns = @JoinColumn(name = "medecin_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_medecin__alerte",
        joinColumns = @JoinColumn(name = "medecin_id"),
        inverseJoinColumns = @JoinColumn(name = "alerte_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patients", "medecins" }, allowSetters = true)
    private Set<Alerte> alertes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "medecins")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "medecins" }, allowSetters = true)
    private Set<Rappel> rappels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medecin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.setMedecin(null));
        }
        if (patients != null) {
            patients.forEach(i -> i.setMedecin(this));
        }
        this.patients = patients;
    }

    public Medecin patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Medecin addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setMedecin(this);
        return this;
    }

    public Medecin removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setMedecin(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Medecin users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Medecin addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Medecin removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Set<Alerte> getAlertes() {
        return this.alertes;
    }

    public void setAlertes(Set<Alerte> alertes) {
        this.alertes = alertes;
    }

    public Medecin alertes(Set<Alerte> alertes) {
        this.setAlertes(alertes);
        return this;
    }

    public Medecin addAlerte(Alerte alerte) {
        this.alertes.add(alerte);
        return this;
    }

    public Medecin removeAlerte(Alerte alerte) {
        this.alertes.remove(alerte);
        return this;
    }

    public Set<Rappel> getRappels() {
        return this.rappels;
    }

    public void setRappels(Set<Rappel> rappels) {
        if (this.rappels != null) {
            this.rappels.forEach(i -> i.removeMedecin(this));
        }
        if (rappels != null) {
            rappels.forEach(i -> i.addMedecin(this));
        }
        this.rappels = rappels;
    }

    public Medecin rappels(Set<Rappel> rappels) {
        this.setRappels(rappels);
        return this;
    }

    public Medecin addRappel(Rappel rappel) {
        this.rappels.add(rappel);
        rappel.getMedecins().add(this);
        return this;
    }

    public Medecin removeRappel(Rappel rappel) {
        this.rappels.remove(rappel);
        rappel.getMedecins().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medecin)) {
            return false;
        }
        return getId() != null && getId().equals(((Medecin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medecin{" +
            "id=" + getId() +
            "}";
    }
}
