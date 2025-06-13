package com.openclassrooms.paymybuddy.dto;

public class UserDTO {
    private String email;
    private String password; // Utilisé uniquement lors de l'enregistrement
    private String friendEmail; // Utilisé pour add-connection
    private double amount; // Utilisé pour dépôt/retrait

    // Constructeurs
    public UserDTO() {}

    public UserDTO(String email, String password, String friendEmail, double amount) {
        this.email = email;
        this.password = password;
        this.friendEmail = friendEmail;
        this.amount = amount;
    }

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
