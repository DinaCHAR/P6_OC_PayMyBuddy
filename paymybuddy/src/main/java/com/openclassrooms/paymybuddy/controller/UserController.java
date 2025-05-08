package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint pour l'inscription d’un nouvel utilisateur.
     * Appelé via POST /api/users/signup
     * On récupère l'objet UserModel depuis le body JSON.
     */
    
    //Inscription d’un nouvel utilisateur
    @PostMapping("/signup")//pour crée un compte user
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel user) {
        try {
        	//utiliser le service pour créer l'utilisateur
            UserModel createdUser = userService.createUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
            );
            //Si tout est ok, retourne 201 CREATED avec le nouvel utilisateur
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
        	//Si une erreur survient (ex : email déjà pris), on retourne 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Endpoint pour la connexion (authentification)
     * POST /api/users/login
     * On envoie un JSON contenant email + mot de passe, et on vérifie leur validité.
     */
    // Connexion sécurisée vérifie email + mot de passe
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (userService.checkPassword(email, password)) {
            // Tu peux générer un token JWT ici si besoin
            return ResponseEntity.ok("Connexion réussie !");
        } else {
        	// Si mauvais identifiants on renvoie 401 UNAUTHORIZED
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects.");
        }
    }

    /**
     * Endpoint pour récupérer un utilisateur à partir de son email
     * GET /api/users/find?email=adresse@example.com
     */
    //Récupération d’un utilisateur par email test
    @GetMapping("/find")
    public ResponseEntity<UserModel> getUserByEmail(@RequestParam String email) {
        try {
            UserModel user = userService.findByEmail(email);
         // 200 OK avec l'utilisateur
            return ResponseEntity.ok(user);
        } catch (Exception e) {
        	//404 si utilisateur non trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
