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
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "sexe", nullable = false)
    private String sexe;

    @NotNull
    @Column(name = "date_de_naissance", nullable = false)
    private LocalDate dateDeNaissance;

    @NotNull
    @Column(name = "num_chambre", nullable = false)
    private Integer numChambre;

    @Column(name = "taille")
    private Float taille;

    @NotNull
    @Column(name = "date_arrivee", nullable = false)
    private LocalDate dateArrivee;

    @Lob
    @Column(name = "info_complementaires")
    private String infoComplementaires;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Mesure> mesures = new HashSet<>();

    @JsonIgnoreProperties(value = { "patient", "medecins" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "patient")
    private Rappel rappel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "patients", "users", "alertes", "rappels" }, allowSetters = true)
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "patients", "users" }, allowSetters = true)
    private Etablissement etablissement;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "patients" }, allowSetters = true)
    private Set<AideSoignant> aideSoignants = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "patients" }, allowSetters = true)
    private Set<Infirmiere> infirmieres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patients" }, allowSetters = true)
    private Set<Repas> repas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patients", "medecins" }, allowSetters = true)
    private Set<Alerte> alertes = new HashSet<>();

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

    public LocalDate getDateArrivee() {
        return this.dateArrivee;
    }

    public Patient dateArrivee(LocalDate dateArrivee) {
        this.setDateArrivee(dateArrivee);
        return this;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
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

    public Set<Mesure> getMesures() {
        return this.mesures;
    }

    public void setMesures(Set<Mesure> mesures) {
        if (this.mesures != null) {
            this.mesures.forEach(i -> i.setPatient(null));
        }
        if (mesures != null) {
            mesures.forEach(i -> i.setPatient(this));
        }
        this.mesures = mesures;
    }

    public Patient mesures(Set<Mesure> mesures) {
        this.setMesures(mesures);
        return this;
    }

    public Patient addMesure(Mesure mesure) {
        this.mesures.add(mesure);
        mesure.setPatient(this);
        return this;
    }

    public Patient removeMesure(Mesure mesure) {
        this.mesures.remove(mesure);
        mesure.setPatient(null);
        return this;
    }

    public Rappel getRappel() {
        return this.rappel;
    }

    public void setRappel(Rappel rappel) {
        if (this.rappel != null) {
            this.rappel.setPatient(null);
        }
        if (rappel != null) {
            rappel.setPatient(this);
        }
        this.rappel = rappel;
    }

    public Patient rappel(Rappel rappel) {
        this.setRappel(rappel);
        return this;
    }

    public Medecin getMedecin() {
        return this.medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Patient medecin(Medecin medecin) {
        this.setMedecin(medecin);
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

    public Set<AideSoignant> getAideSoignants() {
        return this.aideSoignants;
    }

    public void setAideSoignants(Set<AideSoignant> aideSoignants) {
        if (this.aideSoignants != null) {
            this.aideSoignants.forEach(i -> i.removePatient(this));
        }
        if (aideSoignants != null) {
            aideSoignants.forEach(i -> i.addPatient(this));
        }
        this.aideSoignants = aideSoignants;
    }

    public Patient aideSoignants(Set<AideSoignant> aideSoignants) {
        this.setAideSoignants(aideSoignants);
        return this;
    }

    public Patient addAideSoignant(AideSoignant aideSoignant) {
        this.aideSoignants.add(aideSoignant);
        aideSoignant.getPatients().add(this);
        return this;
    }

    public Patient removeAideSoignant(AideSoignant aideSoignant) {
        this.aideSoignants.remove(aideSoignant);
        aideSoignant.getPatients().remove(this);
        return this;
    }

    public Set<Infirmiere> getInfirmieres() {
        return this.infirmieres;
    }

    public void setInfirmieres(Set<Infirmiere> infirmieres) {
        if (this.infirmieres != null) {
            this.infirmieres.forEach(i -> i.removePatient(this));
        }
        if (infirmieres != null) {
            infirmieres.forEach(i -> i.addPatient(this));
        }
        this.infirmieres = infirmieres;
    }

    public Patient infirmieres(Set<Infirmiere> infirmieres) {
        this.setInfirmieres(infirmieres);
        return this;
    }

    public Patient addInfirmiere(Infirmiere infirmiere) {
        this.infirmieres.add(infirmiere);
        infirmiere.getPatients().add(this);
        return this;
    }

    public Patient removeInfirmiere(Infirmiere infirmiere) {
        this.infirmieres.remove(infirmiere);
        infirmiere.getPatients().remove(this);
        return this;
    }

    public Set<Repas> getRepas() {
        return this.repas;
    }

    public void setRepas(Set<Repas> repas) {
        if (this.repas != null) {
            this.repas.forEach(i -> i.removePatient(this));
        }
        if (repas != null) {
            repas.forEach(i -> i.addPatient(this));
        }
        this.repas = repas;
    }

    public Patient repas(Set<Repas> repas) {
        this.setRepas(repas);
        return this;
    }

    public Patient addRepas(Repas repas) {
        this.repas.add(repas);
        repas.getPatients().add(this);
        return this;
    }

    public Patient removeRepas(Repas repas) {
        this.repas.remove(repas);
        repas.getPatients().remove(this);
        return this;
    }

    public Set<Alerte> getAlertes() {
        return this.alertes;
    }

    public void setAlertes(Set<Alerte> alertes) {
        if (this.alertes != null) {
            this.alertes.forEach(i -> i.removePatient(this));
        }
        if (alertes != null) {
            alertes.forEach(i -> i.addPatient(this));
        }
        this.alertes = alertes;
    }

    public Patient alertes(Set<Alerte> alertes) {
        this.setAlertes(alertes);
        return this;
    }

    public Patient addAlerte(Alerte alerte) {
        this.alertes.add(alerte);
        alerte.getPatients().add(this);
        return this;
    }

    public Patient removeAlerte(Alerte alerte) {
        this.alertes.remove(alerte);
        alerte.getPatients().remove(this);
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
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", numChambre=" + getNumChambre() +
            ", taille=" + getTaille() +
            ", dateArrivee='" + getDateArrivee() + "'" +
            ", infoComplementaires='" + getInfoComplementaires() + "'" +
            "}";
    }
}
