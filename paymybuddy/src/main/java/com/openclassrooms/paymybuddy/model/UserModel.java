package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    //CORRECTION APRES ORAL METTRE DEUX DECIMAL ET FAIRE EN SORTE QUE LE MONTANT NE SOIT PAS NULL
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @ManyToMany
    @JoinTable(
        name = "user_connection",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<UserModel> connections;

    public Long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = (long) id;
	}

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

	public BigDecimal getBalance() {
	    return balance;
	}

	public void setBalance(BigDecimal balance) {
	    this.balance = balance;
	}

	public List<UserModel> getConnections() {
		return connections;
	}

	public void setConnections(List<UserModel> connections) {
		this.connections = connections;
	}

	public List<TransactionModel> getSentTransactions() {
		return sentTransactions;
	}

	public void setSentTransactions(List<TransactionModel> sentTransactions) {
		this.sentTransactions = sentTransactions;
	}

	public List<TransactionModel> getReceivedTransactions() {
		return receivedTransactions;
	}

	public void setReceivedTransactions(List<TransactionModel> receivedTransactions) {
		this.receivedTransactions = receivedTransactions;
	}

	@OneToMany(mappedBy = "sender")
    private List<TransactionModel> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<TransactionModel> receivedTransactions;

    // Getters and setters
}