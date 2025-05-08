package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indique que cette classe contient la logique métier
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crée un nouvel utilisateur et le sauvegarde en base de données
    public UserModel createUser(String username, String email, String password) {
        UserModel user = new UserModel(username, email, password);
        return userRepository.save(user); // persist dans la base
    }

    // Recherche d’un utilisateur par email (retourne null si pas trouvé)
    public UserModel findByEmail(String email) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null); // retourne null si absent
    }

    // Vérifie si un utilisateur avec cet email et mot de passe existe
    public boolean checkPassword(String email, String password) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            return user.getPassword().equals(password);
        }
        return false;
    }
}
