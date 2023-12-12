package com.example.instagramclone.Model;

public class User {
private String Nom;
private String Prenom;
private String Pseudo;
private String Email;
private String bio;
private String imageurl;
private String id;

    public User() {
    }
    public User(String nom, String prenom, String pseudo, String email, String bio, String imageurl, String id) {
        Nom = nom;
        Prenom = prenom;
        Pseudo = pseudo;
        Email = email;
        this.bio = bio;
        this.imageurl = imageurl;
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getPseudo() {
        return Pseudo;
    }

    public void setPseudo(String pseudo) {
        Pseudo = pseudo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
