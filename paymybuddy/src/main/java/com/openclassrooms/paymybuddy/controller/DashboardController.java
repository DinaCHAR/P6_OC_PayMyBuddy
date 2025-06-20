package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;

import java.util.Optional;
import org.springframework.ui.Model;

import com.openclassrooms.paymybuddy.model.UserModel;


@Controller // Indiquer que cette classe est un contrôleur Spring MVC
public class DashboardController {

    @Autowired // Injection du service utilisateur
    private UserService userService;

    @Autowired // Injection du service de gestion des connexions
    private ConnectionService connectionService;

    @Autowired // Injection du service de gestion des transactions
    private TransactionService transactionService;

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam String email, Model model) {
        // Recherche de l'utilisateur via son email
        Optional<UserModel> userOpt = userService.findByEmail(email);

        // Redirection vers la page de connexion si l'utilisateur n'existe pas
        if (userOpt.isEmpty()) return "redirect:/login";

        // Préparation des données pour affichage dans la vue dashboard
        UserModel user = userOpt.get();
        model.addAttribute("user", user); // Informations complètes de l'utilisateur
        model.addAttribute("userEmail", user.getEmail()); // Email utilisé dans d'autres vues
        model.addAttribute("connections", connectionService.getConnectionsDTO(email)); // Liste des connexions
        model.addAttribute("transactions", transactionService.getTransactionsByUserEmail(email)); // Historique des transactions

        return "dashboard"; // Renvoie la vue du tableau de bord
    }

    @PostMapping("/dashboard/transfer")
    public String handleTransfer(@RequestParam String senderEmail,
                                 @RequestParam String receiverEmail,
                                 @RequestParam double amount,
                                 @RequestParam String description) {
        // Vérifie l'existence des utilisateurs (émetteur et récepteur)
        Optional<UserModel> senderOpt = userService.findByEmail(senderEmail);
        Optional<UserModel> receiverOpt = userService.findByEmail(receiverEmail);

        // Si les deux utilisateurs existent, exécution du transfert
        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            transactionService.transfer(senderOpt.get(), receiverOpt.get(), amount, description);

            // Redirection vers le dashboard de l'émetteur pour affichage mis à jour
            return "redirect:/dashboard?email=" + senderEmail;
        }

        // Redirection vers la page de connexion si un des deux utilisateurs n'existe pas
        return "redirect:/login";
    }
}

