package com.alfaconseil.bacprep.dto;

public class ChatRequest {
    private String message;
    private Long matiereId;
    private String context;

    public ChatRequest() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getMatiereId() { return matiereId; }
    public void setMatiereId(Long matiereId) { this.matiereId = matiereId; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}
