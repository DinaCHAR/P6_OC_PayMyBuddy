package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indique que c’est un service métier géré par Spring
public class UserService {

    @Autowired
    private UserRepository userRepository; // Injection du repository pour interagir avec la base de données

    // 🔍 Trouver un utilisateur par son email
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // ➕ Créer un nouvel utilisateur (inscription)
    public UserModel createUser(String username, String email, String password) {
        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Création d’un nouvel utilisateur
        UserModel user = new UserModel(username, email, password);
        
        // Sauvegarde dans la base de données
        return userRepository.save(user);
    }

    // 💥 Pour ajouter d'autres méthodes comme la mise à jour ou la suppression des utilisateurs, par exemple :
    // public User updateUser(User user) { ... }
    // public void deleteUser(Integer id) { ... }
}
