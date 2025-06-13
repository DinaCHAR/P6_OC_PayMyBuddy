package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel registerUser(String email, String password) {
    	UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);
        user.setBalance(0.0);
        return userRepository.save(user);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addConnection(UserModel user, UserModel connection) {
        user.getConnections().add(connection);
        userRepository.save(user);
    }

    public void addFunds(UserModel user, double amount) {
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public void withdrawFunds(UserModel user, double amount) {
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Solde insuffisant.");
        }
    }
}