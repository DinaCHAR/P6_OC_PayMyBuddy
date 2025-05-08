package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions") // La table 'transactions' dans la base de données
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // ID unique pour chaque transaction

    @ManyToOne // Un utilisateur qui envoie l'argent (clé étrangère vers 'users')
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;

    @ManyToOne // Un utilisateur qui reçoit l'argent (clé étrangère vers 'users')
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserModel receiver;

    @Column(nullable = false)
    private Double amount; // Montant de la transaction

    @Column
    private String description; // Description de la transaction (facultatif)

    @Column(nullable = false)
    private String date; // Date de la transaction (format de date à définir)

    // Constructeur pour initialiser une transaction
    public TransactionModel(UserModel sender, UserModel receiver, Double amount, String description, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
