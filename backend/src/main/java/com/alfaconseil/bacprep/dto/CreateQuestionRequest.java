package com.alfaconseil.bacprep.dto;

import java.util.List;

public class CreateQuestionRequest {

    private String enonce;
    private String explication;
    private Integer ordre;
    private Double points;
    private List<OptionRequest> options;

    public static class OptionRequest {

        private String texte;
        private Boolean estCorrecte;
        private Integer ordre;

        public OptionRequest() {
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

    public CreateQuestionRequest() {
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

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}