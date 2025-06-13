package com.openclassrooms.paymybuddy.dto;

public class ConnectionResponseDTO {
    private String userEmail;
    private String connectionEmail;

    public ConnectionResponseDTO(String userEmail, String connectionEmail) {
        this.userEmail = userEmail;
        this.connectionEmail = connectionEmail;
    }

    // Getters
    public String getUserEmail() {
        return userEmail;
    }

    public String getConnectionEmail() {
        return connectionEmail;
    }
}

