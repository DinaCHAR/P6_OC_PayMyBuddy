package com.openclassrooms.paymybuddy.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Account: est le compte applicatif utilisé pour stocker de l’argent et faire des transferts au sein de l’app.
 * */

@Entity // Indique que cette classe est une entité JPA correspond à une table en base de données
public class AccountModel {

    @Id // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID
    private Integer id;

    @OneToOne  // un seul compte Account par utilisateur.
    @JoinColumn(name = "user_id", nullable = false, unique = true)// clé étrangère
    private UserModel user;

    @Column(nullable = false, precision = 10, scale = 2) // Colonne non nulle + correction après oral montant avec 2 décimales
    private BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);; // Solde du compte, initialisé à 0, represente le solde dans l'app

    // Constructeur par defaut
    public AccountModel() {
    	
    }

    //Constructeur avec utilisateur, utile pour lier un compte à un utilisateur lors de la création
    public AccountModel(UserModel user) {
        this.user = user;
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP); //Correction apres oral 2 decimal Initialisation du solde
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }
    
 // Méthode pour créditer le compte (ajouter de l'argent)
    public void credit(double amount) {
    	//correction pour avoir 2 decimal pour credite
    	BigDecimal credit = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        this.balance = this.balance.add(credit);
    }

 // Méthode pour débiter le compte (retirer de l'argent)
    public void debit(double amount) {
    	//correction pour avoir 2 decimal pour debiter
    	BigDecimal debit = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        if (this.balance.compareTo(debit) >= 0) {
            this.balance = this.balance.subtract(debit); // Débit autorisé
        } else {
        	// Si le solde est insuffisant, on lance une exception
            throw new IllegalArgumentException("Solde insuffisant.");
        }
    }
}
