package main.java.org.qcm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String email;
    private List<QCMRecord> completedQCMs; // Liste des QCM effectués

    // Constructeur par défaut pour Jackson
    public User() {
        this.completedQCMs = new ArrayList<>();
    }

    // Constructeur principal
    public User(String username, String email) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.completedQCMs = new ArrayList<>();
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<QCMRecord> getCompletedQCMs() {
        return completedQCMs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompletedQCMs(List<QCMRecord> completedQCMs) {
        this.completedQCMs = completedQCMs;
    }

    // Méthode pour ajouter un QCM avec son score
    public void addCompletedQCM(String qcmId, String nom, int score) {
        this.completedQCMs.add(new QCMRecord(qcmId, nom, score));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", completedQCMs=" + completedQCMs +
                '}';
    }
}
