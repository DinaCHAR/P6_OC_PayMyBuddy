package com.openclassrooms.paymybuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.paymybuddy.dto.ConnectionDTO;
import com.openclassrooms.paymybuddy.service.ConnectionService;

@Controller // Définir cette classe comme un contrôleur Spring MVC
public class AddConnectionWebController {

    @Autowired // Injecter automatiquement le service gérant les connexions
    private ConnectionService connectionService;

    @GetMapping("/add-connection")
    public String showAddConnectionForm(@RequestParam String email, Model model) {
        // Ajouter l'adresse email à la vue pour affichage dans le formulaire
        model.addAttribute("userEmail", email);
        return "add-connection"; // Retourne la page du formulaire d'ajout
    }

    @PostMapping("/add-connection")
    public String handleAddConnection(@RequestParam String userEmail,
                                      @RequestParam String connectionEmail,
                                      RedirectAttributes redirectAttributes) {
        boolean success = connectionService.addConnection(userEmail, connectionEmail);

        //redirectAttributes.addAttribute("email", userEmail);
        
        if (!success) {
            redirectAttributes.addFlashAttribute("errorMessage", "La connexion existe déjà ou une erreur est survenue.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Connexion ajoutée avec succès !");
        }

        return "redirect:/connections?email=" + userEmail;
    }

    @GetMapping("/connections")
    public String showConnections(@RequestParam String email, Model model, @ModelAttribute("message") String message, @ModelAttribute("errorMessage") String errorMessage) {
        // Récupèrer les connexions de l'utilisateur sous forme de DTO
        List<ConnectionDTO> connections = connectionService.getConnectionsDTO(email);

        // Ajouter les connexions et l'adresse email au modèle pour la vue
        model.addAttribute("connections", connections);
        model.addAttribute("userEmail", email);
        
        if (!message.isBlank()) {
            model.addAttribute("message", message);
        }
        if (!errorMessage.isBlank()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "connections"; // Retourner la page listant les connexions
    }
}
