package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Indique que c’est un service métier géré par Spring
public class UserService {

    @Autowired
    private UserRepository userRepository; // Injection du repository pour interagir avec la base de données
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserModel createUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // 🔐 Encode le mot de passe avant de le sauvegarder
        String encodedPassword = passwordEncoder.encode(password);
        UserModel user = new UserModel(username, email, encodedPassword);
        return userRepository.save(user);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Vérifie le mot de passe au login
    public boolean checkPassword(String email, String rawPassword) {
        UserModel user = findByEmail(email);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
