package com.alfaconseil.bacprep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "options_reponse")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OptionReponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texte;

    @Column(name = "est_correcte")
    private Boolean estCorrecte = false;

    @Column(name = "ordre")
    private Integer ordre = 0;

    public OptionReponse() {
    }

    public OptionReponse(Long id, Question question, String texte, Boolean estCorrecte, Integer ordre) {
        this.id = id;
        this.question = question;
        this.texte = texte;
        this.estCorrecte = estCorrecte;
        this.ordre = ordre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Boolean getEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(Boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
