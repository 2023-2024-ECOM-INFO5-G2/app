package polytech.g02.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AideSoignant.
 */
@Entity
@Table(name = "aide_soignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AideSoignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_aide_soignant__user",
        joinColumns = @JoinColumn(name = "aide_soignant_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_aide_soignant__patient",
        joinColumns = @JoinColumn(name = "aide_soignant_id"),
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

    public AideSoignant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public AideSoignant users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public AideSoignant addUser(User user) {
        this.users.add(user);
        return this;
    }

    public AideSoignant removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public AideSoignant patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public AideSoignant addPatient(Patient patient) {
        this.patients.add(patient);
        return this;
    }

    public AideSoignant removePatient(Patient patient) {
        this.patients.remove(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AideSoignant)) {
            return false;
        }
        return getId() != null && getId().equals(((AideSoignant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AideSoignant{" +
            "id=" + getId() +
            "}";
    }
}
