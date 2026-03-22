package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "matieres")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 10, unique = true)
    private String code;

    @Column(length = 500)
    private String description;

    @Column(length = 10)
    private String couleur;

    @Column(length = 50)
    private String icone;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage = 0;

    public Matiere() {
    }

    public Matiere(Long id, String nom, String code, String description,
                   String couleur, String icone, Integer ordreAffichage) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.description = description;
        this.couleur = couleur;
        this.icone = icone;
        this.ordreAffichage = ordreAffichage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public Integer getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }
}
