package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;

import org.springframework.ui.Model;

@Controller // Déclarer cette classe comme un contrôleur Spring MVC
public class RegisterController {

    @Autowired // Injecter le service qui gère les opérations sur les utilisateurs
    private UserService userService;

    @PostMapping("/register")
    public String handleRegister(@RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        // Crée et enregistre un nouvel utilisateur avec l'email et le mot de passe fournis
        UserModel user = userService.registerUser(email, password);

        // Ajoute l'utilisateur au modèle pour affichage dans la vue
        model.addAttribute("user", user);

        // Renvoie la vue "profile" après inscription réussie
        return "profile";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
    	 model.addAttribute("success", "Inscription réussie !");
        return "register"; // Affiche le formulaire d'inscription
    }
}

