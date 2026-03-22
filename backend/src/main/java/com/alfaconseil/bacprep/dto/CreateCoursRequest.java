package com.alfaconseil.bacprep.dto;

public class CreateCoursRequest {

    private Long matiereId;
    private String titre;
    private String chapitre;
    private Integer numeroChapitre;
    private String contenu;
    private String resume;
    private String type; // "COURS", "RESUME", "FICHE"
    private String niveau; // "PREMIERE", "TERMINAL"
    private Integer dureeLecture;

    public CreateCoursRequest() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Integer getDureeLecture() {
        return dureeLecture;
    }

    public void setDureeLecture(Integer dureeLecture) {
        this.dureeLecture = dureeLecture;
    }
}