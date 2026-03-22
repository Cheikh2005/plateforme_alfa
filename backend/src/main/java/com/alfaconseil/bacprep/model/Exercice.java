package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exercices")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String enonce;

    @Column(columnDefinition = "LONGTEXT")
    private String correction;

    @Enumerated(EnumType.STRING)
    private Difficulte difficulte = Difficulte.MOYEN;

    @Enumerated(EnumType.STRING)
    private TypeExercice type = TypeExercice.EXERCICE;

    @Column(name = "annee_bac")
    private Integer anneeBac;

    @Column(name = "duree_minutes")
    private Integer dureeMinutes = 20;

    @Column(name = "points_total")
    private Integer pointsTotal = 20;

    @OneToMany(mappedBy = "exercice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "actif")
    private Boolean actif = true;

    public enum Difficulte {
        FACILE, MOYEN, DIFFICILE
    }

    public enum TypeExercice {
        EXERCICE, QCM, ANNALE
    }

    public Exercice() {
    }

    public Exercice(Long id, Matiere matiere, String titre, String enonce, String correction,
                    Difficulte difficulte, TypeExercice type, Integer anneeBac,
                    Integer dureeMinutes, Integer pointsTotal, List<Question> questions,
                    LocalDateTime dateCreation, Boolean actif) {
        this.id = id;
        this.matiere = matiere;
        this.titre = titre;
        this.enonce = enonce;
        this.correction = correction;
        this.difficulte = difficulte;
        this.type = type;
        this.anneeBac = anneeBac;
        this.dureeMinutes = dureeMinutes;
        this.pointsTotal = pointsTotal;
        this.questions = questions;
        this.dateCreation = dateCreation;
        this.actif = actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public TypeExercice getType() {
        return type;
    }

    public void setType(TypeExercice type) {
        this.type = type;
    }

    public Integer getAnneeBac() {
        return anneeBac;
    }

    public void setAnneeBac(Integer anneeBac) {
        this.anneeBac = anneeBac;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Integer getPointsTotal() {
        return pointsTotal;
    }

    public void setPointsTotal(Integer pointsTotal) {
        this.pointsTotal = pointsTotal;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}
