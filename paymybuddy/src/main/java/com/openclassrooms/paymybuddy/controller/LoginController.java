package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller // Définit cette classe comme un contrôleur Spring MVC
public class LoginController {

    @Autowired // Injection du service permettant de gérer les utilisateurs
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        // Recherche de l'utilisateur par email
        Optional<UserModel> userOpt = userService.findByEmail(email);

        // Vérification des identifiants : si l'utilisateur existe et que le mot de passe est correct
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            UserModel user = userOpt.get();

            // Passage des informations de l'utilisateur à la vue
            model.addAttribute("user", user);
            return "profile"; // Affiche la page profil
        } else {
            // En cas d'erreur d'identifiants, message d'erreur affiché sur la vue login
            model.addAttribute("error", "Email ou mot de passe invalide.");
            return "login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Affiche le formulaire de connexion
    }

    @GetMapping("/profile")
    public String showProfile(@RequestParam String email, Model model) {
        // Récupération des informations de l'utilisateur pour affichage du profil
        Optional<UserModel> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "profile"; // Affiche la vue du profil
        }
        return "redirect:/login"; // Redirige vers la connexion si l'utilisateur n'existe pas
    }

    @GetMapping("/profile/edit")
    public String editProfile(@RequestParam String email, Model model) {
        // Récupère les données pour modification du profil, valeur par défaut si email null
        Optional<UserModel> userOpt = userService.findByEmail(email != null ? email : "default@mail.com");

        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "edit-profile"; // Affiche le formulaire d'édition
        }

        return "redirect:/login"; // Redirige vers login si l'utilisateur n'existe pas
    }

    @GetMapping("/profile/save")
    public String saveProfile(@RequestParam String email,
                              @RequestParam String password,
                              Model model) {

        // Recherche de l'utilisateur par email
        Optional<UserModel> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            // Met à jour les informations et les enregistre
            UserModel user = userOpt.get();
            user.setEmail(email);
            user.setPassword(password);
            userService.updateUser(user);

            // Réinjecte les données mises à jour dans le modèle
            model.addAttribute("user", user);
            return "profile"; // Retourne à la vue du profil avec les données à jour
        }

        return "redirect:/login"; // Redirige vers login si l'utilisateur est introuvable
    }
}
