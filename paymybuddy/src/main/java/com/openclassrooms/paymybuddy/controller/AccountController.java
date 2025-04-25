package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.AccountService;
import com.openclassrooms.paymybuddy.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController // Indique que cette classe gère des requêtes REST
@RequestMapping("/account") // Toutes les routes commencent par /account
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    // Obtenir les infos de compte de l’utilisateur connecté
    @GetMapping
    public ResponseEntity<?> getAccount(Principal principal) {
        String email = principal.getName(); // Récupère l’e-mail de l’utilisateur connecté
        UserModel user = userService.findByEmail(email);
        Optional<AccountModel> accountOpt = accountService.getAccountByUser(user);

        return accountOpt
                .map(account -> ResponseEntity.ok().body(account))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Créditer le compte de l’utilisateur
    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestParam double amount, Principal principal) {
        String email = principal.getName();
        UserModel user = userService.findByEmail(email);

        try {
            accountService.creditAccount(user, amount);
            return ResponseEntity.ok("Compte crédité de " + amount + " €");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    //Débiter le compte de l’utilisateur
    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestParam double amount, Principal principal) {
        String email = principal.getName();
        UserModel user = userService.findByEmail(email);

        try {
            accountService.debitAccount(user, amount);
            return ResponseEntity.ok("Compte débité de " + amount + " €");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
