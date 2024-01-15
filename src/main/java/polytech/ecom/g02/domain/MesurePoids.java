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
 * A MesurePoids.
 */
@Entity
@Table(name = "mesure_poids")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MesurePoids implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "2")
    @DecimalMax(value = "635")
    @Column(name = "valeur", nullable = false)
    private Float valeur;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "alertes", "rappels", "mesurePoids", "mesureEPAS", "mesureAlbumines", "repas", "users", "etablissement" },
        allowSetters = true
    )
    private Patient patient;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mesurePoids")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "mesureEPA", "mesureAlbumine", "mesurePoids" }, allowSetters = true)
    private Set<Alerte> alertes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MesurePoids id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValeur() {
        return this.valeur;
    }

    public MesurePoids valeur(Float valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public MesurePoids date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MesurePoids patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Set<Alerte> getAlertes() {
        return this.alertes;
    }

    public void setAlertes(Set<Alerte> alertes) {
        if (this.alertes != null) {
            this.alertes.forEach(i -> i.setMesurePoids(null));
        }
        if (alertes != null) {
            alertes.forEach(i -> i.setMesurePoids(this));
        }
        this.alertes = alertes;
    }

    public MesurePoids alertes(Set<Alerte> alertes) {
        this.setAlertes(alertes);
        return this;
    }

    public MesurePoids addAlerte(Alerte alerte) {
        this.alertes.add(alerte);
        alerte.setMesurePoids(this);
        return this;
    }

    public MesurePoids removeAlerte(Alerte alerte) {
        this.alertes.remove(alerte);
        alerte.setMesurePoids(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MesurePoids)) {
            return false;
        }
        return getId() != null && getId().equals(((MesurePoids) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MesurePoids{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
