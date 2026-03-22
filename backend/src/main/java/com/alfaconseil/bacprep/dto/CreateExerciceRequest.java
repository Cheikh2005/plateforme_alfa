package com.alfaconseil.bacprep.dto;

public class CreateExerciceRequest {

    private Long matiereId;
    private String titre;
    private String enonce;
    private String correction;
    private String difficulte; // "FACILE", "MOYEN", "DIFFICILE"
    private String type; // "EXERCICE", "QCM", "ANNALE"
    private Integer anneeBac;
    private Integer dureeMinutes;
    private Integer pointsTotal;

    public CreateExerciceRequest() {
    }

    public Long getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
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

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}