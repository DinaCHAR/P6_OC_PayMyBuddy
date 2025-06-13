package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.AccountDTO;
import com.openclassrooms.paymybuddy.dto.AccountResponseDTO;
import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.AccountService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Déclare ce contrôleur comme un contrôleur REST qui retourne des données JSON
@RestController

// Préfixe de toutes les routes définies ici : /api/account
@RequestMapping("/api/account")
public class AccountController {

    // Injection du service de gestion de compte
    @Autowired
    private AccountService accountService;

    // Injection du service utilisateur
    @Autowired
    private UserService userService;

    /**
     * Récupère les informations de compte d’un utilisateur à partir de son email.
     * Requête GET vers /api/account?email=...
     */
    @GetMapping
    public ResponseEntity<?> getAccount(@RequestParam("email") String email) {
    	// Recherche de l'utilisateur par email
        Optional<UserModel> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // utilisateur non trouvé
        }

     // Recherche du compte associé à l'utilisateur
        Optional<AccountModel> accountOpt = accountService.getAccountByUser(userOpt.get());

     // Retourne le compte s’il existe, sinon 404
        return accountOpt
            .map(account -> {
                AccountResponseDTO dto = new AccountResponseDTO(
                    userOpt.get().getEmail(),
                    account.getBalance()
                );
                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Créditer le compte de l'utilisateur.
     * Requête POST vers /api/account/credit
     * Paramètres : email, amount
     */
    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestBody AccountDTO request) {
        // Vérifie que l'utilisateur existe
        Optional<UserModel> userOpt = userService.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur introuvable");
        }

        try {
            // Créditer le compte via le service
            accountService.creditAccount(userOpt.get(), request.getAmount());
            return ResponseEntity.ok("Compte crédité de " + request.getAmount() + " €");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    /**
     * Débiter le compte de l'utilisateur.
     * Requête POST vers /api/account/debit
     * Paramètres : email, amount
     */
    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestBody AccountDTO request) {
        // Vérifie que l'utilisateur existe
        Optional<UserModel> userOpt = userService.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur introuvable");
        }

        try {
            // Débiter le compte via le service
            accountService.debitAccount(userOpt.get(), request.getAmount());
            return ResponseEntity.ok("Compte débité de " + request.getAmount() + " €");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
