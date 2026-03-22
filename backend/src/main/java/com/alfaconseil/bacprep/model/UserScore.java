package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_scores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercice_id", nullable = false)
    private Exercice exercice;

    @Column(nullable = false)
    private Double score;

    @Column(name = "score_max")
    private Double scoreMax;

    @Column(name = "temps_passe")
    private Integer tempsPasseMinutes;

    @Column(name = "date_passage")
    private LocalDateTime datePassage = LocalDateTime.now();

    public UserScore() {
    }

    public UserScore(Long id, User user, Exercice exercice, Double score,
                     Double scoreMax, Integer tempsPasseMinutes, LocalDateTime datePassage) {
        this.id = id;
        this.user = user;
        this.exercice = exercice;
        this.score = score;
        this.scoreMax = scoreMax;
        this.tempsPasseMinutes = tempsPasseMinutes;
        this.datePassage = datePassage;
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

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(Double scoreMax) {
        this.scoreMax = scoreMax;
    }

    public Integer getTempsPasseMinutes() {
        return tempsPasseMinutes;
    }

    public void setTempsPasseMinutes(Integer tempsPasseMinutes) {
        this.tempsPasseMinutes = tempsPasseMinutes;
    }

    public LocalDateTime getDatePassage() {
        return datePassage;
    }

    public void setDatePassage(LocalDateTime datePassage) {
        this.datePassage = datePassage;
    }
}
