package polytech.ecom.g02.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 64)
    @Column(name = "nom", length = 64)
    private String nom;

    @NotNull
    @Size(min = 3, max = 128)
    @Column(name = "adresse", length = 128, nullable = false)
    private String adresse;

    @NotNull
    @Size(min = 3, max = 64)
    @Column(name = "ville", length = 64, nullable = false)
    private String ville;

    @NotNull
    @Size(min = 5, max = 5)
    @Pattern(regexp = "^\\d{5}$")
    @Column(name = "code_postal", length = 5, nullable = false)
    private String codePostal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "alertes", "rappels", "mesurePoids", "mesureEPAS", "mesureAlbumines", "repas", "users", "etablissement" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_etablissement__user",
        joinColumns = @JoinColumn(name = "etablissement_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Etablissement nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Etablissement adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return this.ville;
    }

    public Etablissement ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Etablissement codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.setEtablissement(null));
        }
        if (patients != null) {
            patients.forEach(i -> i.setEtablissement(this));
        }
        this.patients = patients;
    }

    public Etablissement patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Etablissement addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setEtablissement(this);
        return this;
    }

    public Etablissement removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setEtablissement(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Etablissement users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Etablissement addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Etablissement removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return getId() != null && getId().equals(((Etablissement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            "}";
    }
}
