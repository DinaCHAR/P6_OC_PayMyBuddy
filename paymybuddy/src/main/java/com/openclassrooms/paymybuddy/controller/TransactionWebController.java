package com.openclassrooms.paymybuddy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.ConnectionService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;

@Controller // Indiquer que cette classe est un contrôleur Spring MVC
public class TransactionWebController {

    @Autowired // Injection du service de gestion des transactions
    private TransactionService transactionService;

    @Autowired // Injection du service de gestion des utilisateurs
    private UserService userService;

    @Autowired // Injection du service de gestion des connexions
    private ConnectionService connectionService;

    @PostMapping("/transfer")
    public String transfer(TransactionDTO dto, Model model) {
        // Recherche de l'expéditeur et du destinataire à partir de leurs emails
        Optional<UserModel> senderOpt = userService.findByEmail(dto.getSenderMail());
        Optional<UserModel> receiverOpt = userService.findByEmail(dto.getReceiverMail());

        // Vérifie que les deux utilisateurs existent
        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            // Ajoute un message d'erreur s'il en manque un
            model.addAttribute("error", "Utilisateur expéditeur ou destinataire introuvable.");
        } else {
            try {
                // Si tout est valide, effectue le transfert via le service
                transactionService.transfer(
                    senderOpt.get(),
                    receiverOpt.get(),
                    dto.getAmount(),
                    dto.getDescription()
                );
                // Redirige vers le tableau de bord de l'expéditeur
                return "redirect:/dashboard?email=" + dto.getSenderMail();
            } catch (IllegalArgumentException e) {
                // Gestion d'exception métier, par exemple en cas de solde insuffisant
                model.addAttribute("error", e.getMessage());
            }
        }

        // En cas d'erreur, recharge le tableau de bord avec les données utiles
        String email = dto.getSenderMail();
        model.addAttribute("user", senderOpt.orElse(null));
        model.addAttribute("connections", connectionService.getConnectionsDTO(email));
        model.addAttribute("transactions", transactionService.getTransactionsByUserEmail(email));
        model.addAttribute("success", "Transfert effectué avec succès !");
        return "dashboard"; // Affiche la vue dashboard avec le message d'erreur
    }
}
