package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.UserDTO;
import com.openclassrooms.paymybuddy.dto.UserResponseDTO;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Cette classe est un contrôleur REST
@RestController

// Définit le préfixe commun de toutes les routes : /api/users
@RequestMapping("/api/users")
public class UserController {

    // Injection automatique du service utilisateur
    @Autowired
    private UserService userService;

    /**
     * Endpoint pour enregistrer un nouvel utilisateur.
     * Requête POST vers /api/users/register
     * Nécessite email et password dans le corps de la requête.
     */
    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserDTO userDTO) {
        UserModel user = userService.registerUser(userDTO.getEmail(), userDTO.getPassword());
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getBalance());
    }
    /**
     * Endpoint pour ajouter un ami à la liste de connexions.
     * Requête POST vers /api/users/add-connection
     * Nécessite userEmail (celui qui ajoute) et friendEmail (l'ami à ajouter)
     */
    @PostMapping("/add-connection")
    public String addConnection(@RequestBody UserDTO userDTO) {

        // Récupération des deux utilisateurs à partir de leur email
        Optional<UserModel> userOpt = userService.findByEmail(userDTO.getEmail());
        Optional<UserModel> friendOpt = userService.findByEmail(userDTO.getFriendEmail());

        // Vérifie si les deux utilisateurs existent avant d’ajouter la connexion
        if (userOpt.isPresent() && friendOpt.isPresent()) {
            userService.addConnection(userOpt.get(), friendOpt.get());
            return "Connexion ajoutée";
        }

        return "Utilisateur ou ami introuvable";
    }

    /**
     * Endpoint pour créditer un utilisateur.
     * Requête POST vers /api/users/deposit
     * Nécessite email et montant
     */
    @PostMapping("/deposit")
    public String deposit(@RequestBody UserDTO userDTO) {

        // Recherche de l'utilisateur
        Optional<UserModel> userOpt = userService.findByEmail(userDTO.getEmail());

        if (userOpt.isPresent()) {
            userService.addFunds(userOpt.get(), userDTO.getAmount());
            return "Dépôt effectué";
        }

        return "Utilisateur introuvable";
    }

    /**
     * Endpoint pour retirer de l'argent du compte utilisateur.
     * Requête POST vers /api/users/withdraw
     * Nécessite email et montant
     */
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody UserDTO userDTO) {

        Optional<UserModel> userOpt = userService.findByEmail(userDTO.getEmail());

        if (userOpt.isPresent()) {
            try {
                userService.withdrawFunds(userOpt.get(), userDTO.getAmount());
                return "Retrait effectué";
            } catch (IllegalArgumentException e) {
                return e.getMessage(); // Ex : si solde insuffisant
            }
        }

        return "Utilisateur introuvable";
    }

    /**
     * Endpoint pour rechercher un utilisateur à partir de son email.
     * Requête GET vers /api/users/find?email=...
     */
    @GetMapping("/find")
    public ResponseEntity<?> findUser(@RequestParam("email") String email) {
        Optional<UserModel> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getEmail(), user.getBalance());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body("Utilisateur introuvable");
        }
    }
}
