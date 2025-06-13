package com.openclassrooms.paymybuddy.dto;

public class ConnectionDTO {
	
    private String userEmail;
    private String connectionEmail;

    // Constructeurs
    public ConnectionDTO() {}

    public ConnectionDTO(String userEmail, String connectionEmail) {
        this.userEmail = userEmail;
        this.connectionEmail = connectionEmail;
    }

    // Getters et Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getConnectionEmail() {
        return connectionEmail;
    }

    public void setConnectionEmail(String connectionEmail) {
        this.connectionEmail = connectionEmail;
    }
}