package com.alfaconseil.bacprep.dto;

import java.util.Map;

public class SubmitAnswerRequest {
    private Long exerciceId;
    private Map<Long, Long> reponses;
    private Integer tempsPasseMinutes;

    public SubmitAnswerRequest() {}

    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }

    public Map<Long, Long> getReponses() { return reponses; }
    public void setReponses(Map<Long, Long> reponses) { this.reponses = reponses; }

    public Integer getTempsPasseMinutes() { return tempsPasseMinutes; }
    public void setTempsPasseMinutes(Integer tempsPasseMinutes) { this.tempsPasseMinutes = tempsPasseMinutes; }
}
