package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bank_account")
public class BankAccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String bankName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
