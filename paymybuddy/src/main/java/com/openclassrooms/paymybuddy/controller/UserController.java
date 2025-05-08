package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // Indique que cette classe gère des requêtes REST
@RequestMapping("/api/users") // Toutes les routes commencent par /api/users
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService; // Service pour gérer la génération du JWT

    /**
     * Endpoint pour l'inscription d’un nouvel utilisateur.
     * POST /api/users/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel user) {
        try {
            // Création du nouvel utilisateur via le service
            UserModel createdUser = userService.createUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Endpoint pour la connexion (authentification).
     * POST /api/users/login
     * Renvoie un token JWT si les identifiants sont valides.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (userService.checkPassword(email, password)) {
            // Génère un JWT à partir de l'email de l'utilisateur
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(token); // Retourne le token JWT au client
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects.");
        }
    }

    /**
     * Endpoint pour récupérer un utilisateur à partir de son email.
     * GET /api/users/find?email=exemple@mail.com
     */
    @GetMapping("/find")
    public ResponseEntity<UserModel> getUserByEmail(@RequestParam String email) {
        try {
            UserModel user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
