package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cours")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 100)
    private String chapitre;

    @Column(name = "numero_chapitre")
    private Integer numeroChapitre;

    @Column(columnDefinition = "LONGTEXT")
    private String contenu;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCours type = TypeCours.COURS;

    @Enumerated(EnumType.STRING)
    private Niveau niveau = Niveau.TERMINAL;

    @Column(name = "duree_lecture")
    private Integer dureeLecture = 30;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "actif")
    private Boolean actif = true;

    public enum TypeCours {
        COURS, RESUME, FICHE
    }

    public enum Niveau {
        PREMIERE, TERMINAL
    }

    public Cours() {
    }

    public Cours(Long id, Matiere matiere, String titre, String chapitre, Integer numeroChapitre,
                 String contenu, String resume, TypeCours type, Niveau niveau,
                 Integer dureeLecture, LocalDateTime dateCreation, Boolean actif) {
        this.id = id;
        this.matiere = matiere;
        this.titre = titre;
        this.chapitre = chapitre;
        this.numeroChapitre = numeroChapitre;
        this.contenu = contenu;
        this.resume = resume;
        this.type = type;
        this.niveau = niveau;
        this.dureeLecture = dureeLecture;
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

    public String getChapitre() {
        return chapitre;
    }

    public void setChapitre(String chapitre) {
        this.chapitre = chapitre;
    }

    public Integer getNumeroChapitre() {
        return numeroChapitre;
    }

    public void setNumeroChapitre(Integer numeroChapitre) {
        this.numeroChapitre = numeroChapitre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public TypeCours getType() {
        return type;
    }

    public void setType(TypeCours type) {
        this.type = type;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Integer getDureeLecture() {
        return dureeLecture;
    }

    public void setDureeLecture(Integer dureeLecture) {
        this.dureeLecture = dureeLecture;
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
