package com.alfaconseil.bacprep.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private String serie;

    public JwtResponse() {}

    public JwtResponse(String token, Long id, String username, String email,
                       String nom, String prenom, String role, String serie) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.serie = serie;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
}
