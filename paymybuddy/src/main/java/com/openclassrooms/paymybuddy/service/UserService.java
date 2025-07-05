package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.Optional;

@Service // Indique que cette classe est un service Spring gérant la logique métier liée aux utilisateurs
@Transactional
public class UserService {

    @Autowired // Injecte automatiquement le repository des utilisateurs
    private UserRepository userRepository;

    // Enregistre un nouvel utilisateur avec un email et un mot de passe
    public UserModel registerUser(String email, String password) {
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);
        //CORRECTION APRES ORAL AJOUT DE SEULEMENT DEUX DECIMAL
        user.setBalance(BigDecimal.ZERO); // Le solde commence à zéro
        return userRepository.save(user); // Sauvegarde dans la base de données
    }

    // Recherche un utilisateur par son email
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Ajoute une connexion (ami, contact...) à un utilisateur
    public void addConnection(UserModel user, UserModel connection) {
        user.getConnections().add(connection); // Ajoute la connexion à la liste
        userRepository.save(user); // Met à jour l'utilisateur
    }

    // Ajoute de l'argent au solde d'un utilisateur
    public void addFunds(UserModel user, double amount) {
    	//CORRECTION APRES ORAL INCREMENTER DEUX DECIMAL
    	user.setBalance(user.getBalance().add(BigDecimal.valueOf(amount))); // Incrémente le solde
        userRepository.save(user);
    }

    // Permet à un utilisateur de retirer de l'argent si son solde est suffisant
    public void withdrawFunds(UserModel user, double amount) {
    	//CORRECTION APRES ORAL DEUX DECIMAL
        if (user.getBalance().compareTo(BigDecimal.valueOf(amount)) >= 0) {
        	user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(amount))); // Décrémente le solde
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Solde insuffisant."); // Déclenche une erreur si le solde est trop faible
        }
    }

    // Met à jour les informations de l'utilisateur
    public void updateUser(UserModel user) {
        userRepository.save(user);
    }
}
