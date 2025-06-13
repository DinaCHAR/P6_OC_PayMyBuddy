package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.model.UserModel;

public class UserResponseDTO {
    private Long id;
    private String email;
    private double balance;

    public UserResponseDTO(Long id, String email, double balance) {
        this.id = id;
        this.email = email;
        this.balance = balance;
    }

    public UserResponseDTO(UserModel user) {
    	this.email = user.getEmail();
        this.balance = user.getBalance();
	}

	// Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
}
