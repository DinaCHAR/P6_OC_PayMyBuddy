package com.openclassrooms.paymybuddy.dto;

public class AccountResponseDTO {
    private String email;
    private double balance;

    public AccountResponseDTO(String email, double balance) {
        this.email = email;
        this.balance = balance;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
}
