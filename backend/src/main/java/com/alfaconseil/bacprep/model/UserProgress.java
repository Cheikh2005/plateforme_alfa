package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "cours_id"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(name = "pourcentage")
    private Integer pourcentage = 0;

    @Column(name = "complete")
    private Boolean complete = false;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut = LocalDateTime.now();

    @Column(name = "date_completion")
    private LocalDateTime dateCompletion;

    public UserProgress() {
    }

    public UserProgress(Long id, User user, Cours cours, Integer pourcentage,
                        Boolean complete, LocalDateTime dateDebut, LocalDateTime dateCompletion) {
        this.id = id;
        this.user = user;
        this.cours = cours;
        this.pourcentage = pourcentage;
        this.complete = complete;
        this.dateDebut = dateDebut;
        this.dateCompletion = dateCompletion;
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

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Integer getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Integer pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(LocalDateTime dateCompletion) {
        this.dateCompletion = dateCompletion;
    }
}
