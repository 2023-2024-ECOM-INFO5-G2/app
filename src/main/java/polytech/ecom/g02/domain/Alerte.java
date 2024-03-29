package polytech.ecom.g02.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alerte.
 */
@Entity
@Table(name = "alerte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 512)
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "severe", nullable = false)
    private Boolean severe;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "alertes", "rappels", "mesurePoids", "mesureEPAS", "mesureAlbumines", "repas", "users", "etablissement" },
        allowSetters = true
    )
    private Patient patient;

    @JsonIgnoreProperties(value = { "patient", "alerte" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MesureEPA mesureEPA;

    @JsonIgnoreProperties(value = { "patient", "alerte" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MesureAlbumine mesureAlbumine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "patient", "alertes" }, allowSetters = true)
    private MesurePoids mesurePoids;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alerte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Alerte description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Alerte date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getSevere() {
        return this.severe;
    }

    public Alerte severe(Boolean severe) {
        this.setSevere(severe);
        return this;
    }

    public void setSevere(Boolean severe) {
        this.severe = severe;
    }

    public Integer getCode() {
        return this.code;
    }

    public Alerte code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Alerte patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public MesureEPA getMesureEPA() {
        return this.mesureEPA;
    }

    public void setMesureEPA(MesureEPA mesureEPA) {
        this.mesureEPA = mesureEPA;
    }

    public Alerte mesureEPA(MesureEPA mesureEPA) {
        this.setMesureEPA(mesureEPA);
        return this;
    }

    public MesureAlbumine getMesureAlbumine() {
        return this.mesureAlbumine;
    }

    public void setMesureAlbumine(MesureAlbumine mesureAlbumine) {
        this.mesureAlbumine = mesureAlbumine;
    }

    public Alerte mesureAlbumine(MesureAlbumine mesureAlbumine) {
        this.setMesureAlbumine(mesureAlbumine);
        return this;
    }

    public MesurePoids getMesurePoids() {
        return this.mesurePoids;
    }

    public void setMesurePoids(MesurePoids mesurePoids) {
        this.mesurePoids = mesurePoids;
    }

    public Alerte mesurePoids(MesurePoids mesurePoids) {
        this.setMesurePoids(mesurePoids);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alerte)) {
            return false;
        }
        return getId() != null && getId().equals(((Alerte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alerte{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", severe='" + getSevere() + "'" +
            ", code=" + getCode() +
            "}";
    }
}
