package polytech.ecom.g02.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "echeance", nullable = false)
    private ZonedDateTime echeance;

    @NotNull
    @Min(value = 1)
    @Max(value = 366)
    @Column(name = "intervale_jours", nullable = false)
    private Integer intervaleJours;

    @NotNull
    @Size(min = 3, max = 512)
    @Column(name = "tache", length = 512, nullable = false)
    private String tache;

    @NotNull
    @Column(name = "fee_dans_letang", nullable = false)
    private Boolean feeDansLetang;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_rappel__user", joinColumns = @JoinColumn(name = "rappel_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "alertes", "rappels", "mesurePoids", "mesureEPAS", "mesureAlbumines", "repas", "users", "etablissement" },
        allowSetters = true
    )
    private Patient patient;

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

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Rappel date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getEcheance() {
        return this.echeance;
    }

    public Rappel echeance(ZonedDateTime echeance) {
        this.setEcheance(echeance);
        return this;
    }

    public void setEcheance(ZonedDateTime echeance) {
        this.echeance = echeance;
    }

    public Integer getIntervaleJours() {
        return this.intervaleJours;
    }

    public Rappel intervaleJours(Integer intervaleJours) {
        this.setIntervaleJours(intervaleJours);
        return this;
    }

    public void setIntervaleJours(Integer intervaleJours) {
        this.intervaleJours = intervaleJours;
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

    public Boolean getFeeDansLetang() {
        return this.feeDansLetang;
    }

    public Rappel feeDansLetang(Boolean feeDansLetang) {
        this.setFeeDansLetang(feeDansLetang);
        return this;
    }

    public void setFeeDansLetang(Boolean feeDansLetang) {
        this.feeDansLetang = feeDansLetang;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Rappel users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Rappel addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Rappel removeUser(User user) {
        this.users.remove(user);
        return this;
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
            ", date='" + getDate() + "'" +
            ", echeance='" + getEcheance() + "'" +
            ", intervaleJours=" + getIntervaleJours() +
            ", tache='" + getTache() + "'" +
            ", feeDansLetang='" + getFeeDansLetang() + "'" +
            "}";
    }
}
