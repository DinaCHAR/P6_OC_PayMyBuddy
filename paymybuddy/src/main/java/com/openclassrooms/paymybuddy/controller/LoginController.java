package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        Optional<UserModel> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            UserModel user = userOpt.get();
            model.addAttribute("user", user);
            return "profile"; // redirige vers la vue profile.html
        } else {
            model.addAttribute("error", "Email ou mot de passe invalide.");
            return "login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @GetMapping("/profile")
    public String showProfile(@RequestParam String email, Model model) {
        Optional<UserModel> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "profile";
        }
        return "redirect:/login"; // ou une page d'erreur
    }
    
    @GetMapping("/profile/edit")
    public String editProfile(@RequestParam String email, Model model) {
        Optional<UserModel> userOpt = userService.findByEmail(email != null ? email : "default@mail.com");

        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "edit-profile";
        }

        return "redirect:/login";
    }
    
    @GetMapping("/profile/save")
    public String saveProfile(
                              @RequestParam String email,
                              @RequestParam String password,
                              Model model) {

        Optional<UserModel> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            user.setEmail(email);
            user.setPassword(password);
            userService.updateUser(user);
            model.addAttribute("user", user);
            return "profile";
        }

        return "redirect:/login";
    }
}	
