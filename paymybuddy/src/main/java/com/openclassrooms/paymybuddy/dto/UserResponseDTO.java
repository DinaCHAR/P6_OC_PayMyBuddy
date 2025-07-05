package com.openclassrooms.paymybuddy.dto;

import java.math.BigDecimal;

import com.openclassrooms.paymybuddy.model.UserModel;

public class UserResponseDTO {
    private Long id;
    private String email;
    //CORRECTION APRES ORAL AJOUT DE DEUX DECIMAL
    private BigDecimal balance;

    public UserResponseDTO(Long id, String email, BigDecimal balance) {
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

    public BigDecimal getBalance() {
        return balance;
    }
}
