package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plannings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "heure_debut", length = 10)
    private String heureDebut;

    @Column(name = "heure_fin", length = 10)
    private String heureFin;

    @Column(name = "couleur", length = 20)
    private String couleur = "#2196F3";

    @Column(name = "complete")
    private Boolean complete = false;

    @Column(name = "rappel")
    private Boolean rappel = false;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    public Planning() {
    }

    public Planning(Long id, User user, Matiere matiere, String titre, String description,
                    LocalDate dateDebut, LocalDate dateFin, String heureDebut, String heureFin,
                    String couleur, Boolean complete, Boolean rappel, LocalDateTime dateCreation) {
        this.id = id;
        this.user = user;
        this.matiere = matiere;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.couleur = couleur;
        this.complete = complete;
        this.rappel = rappel;
        this.dateCreation = dateCreation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Boolean getRappel() {
        return rappel;
    }

    public void setRappel(Boolean rappel) {
        this.rappel = rappel;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
