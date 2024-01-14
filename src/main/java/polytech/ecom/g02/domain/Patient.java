package polytech.ecom.g02.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 64)
    @Column(name = "prenom", length = 64, nullable = false)
    private String prenom;

    @NotNull
    @Size(min = 3, max = 64)
    @Column(name = "nom", length = 64, nullable = false)
    private String nom;

    @NotNull
    @Size(min = 3, max = 64)
    @Column(name = "sexe", length = 64, nullable = false)
    private String sexe;

    @NotNull
    @DecimalMin(value = "45")
    @DecimalMax(value = "290")
    @Column(name = "taille", nullable = false)
    private Float taille;

    @NotNull
    @Column(name = "date_de_naissance", nullable = false)
    private LocalDate dateDeNaissance;

    @NotNull
    @Column(name = "num_chambre", nullable = false)
    private Integer numChambre;

    @NotNull
    @Column(name = "date_arrivee", nullable = false)
    private ZonedDateTime dateArrivee;

    @Size(max = 1024)
    @Column(name = "info_complementaires", length = 1024)
    private String infoComplementaires;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "mesureEPA", "mesureAlbumine", "mesurePoids" }, allowSetters = true)
    private Set<Alerte> alertes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "patient" }, allowSetters = true)
    private Set<Rappel> rappels = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "alertes" }, allowSetters = true)
    private Set<MesurePoids> mesurePoids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "alerte" }, allowSetters = true)
    private Set<MesureEPA> mesureEPAS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "alerte" }, allowSetters = true)
    private Set<MesureAlbumine> mesureAlbumines = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Repas> repas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_patient__user",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "patients", "users" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Patient prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Patient nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return this.sexe;
    }

    public Patient sexe(String sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Float getTaille() {
        return this.taille;
    }

    public Patient taille(Float taille) {
        this.setTaille(taille);
        return this;
    }

    public void setTaille(Float taille) {
        this.taille = taille;
    }

    public LocalDate getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public Patient dateDeNaissance(LocalDate dateDeNaissance) {
        this.setDateDeNaissance(dateDeNaissance);
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public Integer getNumChambre() {
        return this.numChambre;
    }

    public Patient numChambre(Integer numChambre) {
        this.setNumChambre(numChambre);
        return this;
    }

    public void setNumChambre(Integer numChambre) {
        this.numChambre = numChambre;
    }

    public ZonedDateTime getDateArrivee() {
        return this.dateArrivee;
    }

    public Patient dateArrivee(ZonedDateTime dateArrivee) {
        this.setDateArrivee(dateArrivee);
        return this;
    }

    public void setDateArrivee(ZonedDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getInfoComplementaires() {
        return this.infoComplementaires;
    }

    public Patient infoComplementaires(String infoComplementaires) {
        this.setInfoComplementaires(infoComplementaires);
        return this;
    }

    public void setInfoComplementaires(String infoComplementaires) {
        this.infoComplementaires = infoComplementaires;
    }

    public Set<Alerte> getAlertes() {
        return this.alertes;
    }

    public void setAlertes(Set<Alerte> alertes) {
        if (this.alertes != null) {
            this.alertes.forEach(i -> i.setPatient(null));
        }
        if (alertes != null) {
            alertes.forEach(i -> i.setPatient(this));
        }
        this.alertes = alertes;
    }

    public Patient alertes(Set<Alerte> alertes) {
        this.setAlertes(alertes);
        return this;
    }

    public Patient addAlerte(Alerte alerte) {
        this.alertes.add(alerte);
        alerte.setPatient(this);
        return this;
    }

    public Patient removeAlerte(Alerte alerte) {
        this.alertes.remove(alerte);
        alerte.setPatient(null);
        return this;
    }

    public Set<Rappel> getRappels() {
        return this.rappels;
    }

    public void setRappels(Set<Rappel> rappels) {
        if (this.rappels != null) {
            this.rappels.forEach(i -> i.setPatient(null));
        }
        if (rappels != null) {
            rappels.forEach(i -> i.setPatient(this));
        }
        this.rappels = rappels;
    }

    public Patient rappels(Set<Rappel> rappels) {
        this.setRappels(rappels);
        return this;
    }

    public Patient addRappel(Rappel rappel) {
        this.rappels.add(rappel);
        rappel.setPatient(this);
        return this;
    }

    public Patient removeRappel(Rappel rappel) {
        this.rappels.remove(rappel);
        rappel.setPatient(null);
        return this;
    }

    public Set<MesurePoids> getMesurePoids() {
        return this.mesurePoids;
    }

    public void setMesurePoids(Set<MesurePoids> mesurePoids) {
        if (this.mesurePoids != null) {
            this.mesurePoids.forEach(i -> i.setPatient(null));
        }
        if (mesurePoids != null) {
            mesurePoids.forEach(i -> i.setPatient(this));
        }
        this.mesurePoids = mesurePoids;
    }

    public Patient mesurePoids(Set<MesurePoids> mesurePoids) {
        this.setMesurePoids(mesurePoids);
        return this;
    }

    public Patient addMesurePoids(MesurePoids mesurePoids) {
        this.mesurePoids.add(mesurePoids);
        mesurePoids.setPatient(this);
        return this;
    }

    public Patient removeMesurePoids(MesurePoids mesurePoids) {
        this.mesurePoids.remove(mesurePoids);
        mesurePoids.setPatient(null);
        return this;
    }

    public Set<MesureEPA> getMesureEPAS() {
        return this.mesureEPAS;
    }

    public void setMesureEPAS(Set<MesureEPA> mesureEPAS) {
        if (this.mesureEPAS != null) {
            this.mesureEPAS.forEach(i -> i.setPatient(null));
        }
        if (mesureEPAS != null) {
            mesureEPAS.forEach(i -> i.setPatient(this));
        }
        this.mesureEPAS = mesureEPAS;
    }

    public Patient mesureEPAS(Set<MesureEPA> mesureEPAS) {
        this.setMesureEPAS(mesureEPAS);
        return this;
    }

    public Patient addMesureEPA(MesureEPA mesureEPA) {
        this.mesureEPAS.add(mesureEPA);
        mesureEPA.setPatient(this);
        return this;
    }

    public Patient removeMesureEPA(MesureEPA mesureEPA) {
        this.mesureEPAS.remove(mesureEPA);
        mesureEPA.setPatient(null);
        return this;
    }

    public Set<MesureAlbumine> getMesureAlbumines() {
        return this.mesureAlbumines;
    }

    public void setMesureAlbumines(Set<MesureAlbumine> mesureAlbumines) {
        if (this.mesureAlbumines != null) {
            this.mesureAlbumines.forEach(i -> i.setPatient(null));
        }
        if (mesureAlbumines != null) {
            mesureAlbumines.forEach(i -> i.setPatient(this));
        }
        this.mesureAlbumines = mesureAlbumines;
    }

    public Patient mesureAlbumines(Set<MesureAlbumine> mesureAlbumines) {
        this.setMesureAlbumines(mesureAlbumines);
        return this;
    }

    public Patient addMesureAlbumine(MesureAlbumine mesureAlbumine) {
        this.mesureAlbumines.add(mesureAlbumine);
        mesureAlbumine.setPatient(this);
        return this;
    }

    public Patient removeMesureAlbumine(MesureAlbumine mesureAlbumine) {
        this.mesureAlbumines.remove(mesureAlbumine);
        mesureAlbumine.setPatient(null);
        return this;
    }

    public Set<Repas> getRepas() {
        return this.repas;
    }

    public void setRepas(Set<Repas> repas) {
        if (this.repas != null) {
            this.repas.forEach(i -> i.setPatient(null));
        }
        if (repas != null) {
            repas.forEach(i -> i.setPatient(this));
        }
        this.repas = repas;
    }

    public Patient repas(Set<Repas> repas) {
        this.setRepas(repas);
        return this;
    }

    public Patient addRepas(Repas repas) {
        this.repas.add(repas);
        repas.setPatient(this);
        return this;
    }

    public Patient removeRepas(Repas repas) {
        this.repas.remove(repas);
        repas.setPatient(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Patient users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Patient addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Patient removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Patient etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", taille=" + getTaille() +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", numChambre=" + getNumChambre() +
            ", dateArrivee='" + getDateArrivee() + "'" +
            ", infoComplementaires='" + getInfoComplementaires() + "'" +
            "}";
    }
}
