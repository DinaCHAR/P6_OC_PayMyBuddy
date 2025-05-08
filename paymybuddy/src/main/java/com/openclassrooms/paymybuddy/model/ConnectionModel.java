package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_connection")
public class ConnectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "connection_id", nullable = false)
    private UserModel connection;

    public ConnectionModel() {}

    public ConnectionModel(UserModel user, UserModel connection) {
        this.user = user;
        this.connection = connection;
    }

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public UserModel getConnection() { return connection; }
    public void setConnection(UserModel connection) { this.connection = connection; }
}
