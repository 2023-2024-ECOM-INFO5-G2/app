package polytech.ecom.g02.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
    @Size(min = 3, max = 64)
    @Column(name = "nom", length = 64, nullable = false)
    private String nom;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @DecimalMin(value = "1")
    @DecimalMax(value = "5000")
    @Column(name = "apport_calorique")
    private Float apportCalorique;

    @DecimalMin(value = "1")
    @DecimalMax(value = "5000")
    @Column(name = "poids_consomme")
    private Float poidsConsomme;

    @Size(min = 3, max = 512)
    @Column(name = "description", length = 512)
    private String description;

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

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Repas date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Float getApportCalorique() {
        return this.apportCalorique;
    }

    public Repas apportCalorique(Float apportCalorique) {
        this.setApportCalorique(apportCalorique);
        return this;
    }

    public void setApportCalorique(Float apportCalorique) {
        this.apportCalorique = apportCalorique;
    }

    public Float getPoidsConsomme() {
        return this.poidsConsomme;
    }

    public Repas poidsConsomme(Float poidsConsomme) {
        this.setPoidsConsomme(poidsConsomme);
        return this;
    }

    public void setPoidsConsomme(Float poidsConsomme) {
        this.poidsConsomme = poidsConsomme;
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

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Repas patient(Patient patient) {
        this.setPatient(patient);
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
            ", date='" + getDate() + "'" +
            ", apportCalorique=" + getApportCalorique() +
            ", poidsConsomme=" + getPoidsConsomme() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
