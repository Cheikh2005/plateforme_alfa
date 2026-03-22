package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercice_id", nullable = false)
    private Exercice exercice;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Column(name = "explication", columnDefinition = "TEXT")
    private String explication;

    @Column(name = "ordre")
    private Integer ordre = 1;

    @Column(name = "points")
    private Double points = 1.0;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionReponse> options;

    public Question() {
    }

    public Question(Long id, Exercice exercice, String enonce, String explication,
                    Integer ordre, Double points, List<OptionReponse> options) {
        this.id = id;
        this.exercice = exercice;
        this.enonce = enonce;
        this.explication = explication;
        this.ordre = ordre;
        this.points = points;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public List<OptionReponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionReponse> options) {
        this.options = options;
    }
}
