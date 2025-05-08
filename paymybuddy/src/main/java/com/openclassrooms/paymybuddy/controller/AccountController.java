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

//Indique que cette classe est un contrôleur REST (les réponses seront automatiquement converties en JSON)
@RestController

//Définit le préfixe d’URL commun pour tous les endpoints de cette classe
@RequestMapping("/account")
public class AccountController {

	 // Injection du service de gestion de compte (AccountService)
	 @Autowired
	 private AccountService accountService;
	
	 // Injection du service utilisateur pour récupérer les infos de l’utilisateur connecté
	 @Autowired
	 private UserService userService;
	
	 /**
	  * Endpoint GET /account
	  * Permet de récupérer les informations du compte de l’utilisateur connecté.
	  * Utilise l’objet `Principal` pour obtenir l’email de l’utilisateur connecté.
	  */
	 @GetMapping
	 public ResponseEntity<?> getAccount(Principal principal) {
	     // Récupère l’email de l’utilisateur actuellement connecté via Spring Security
	     String email = principal.getName();
	
	     // Récupère l’objet UserModel associé à cet email
	     UserModel user = userService.findByEmail(email);
	
	     // Tente de retrouver un compte associé à cet utilisateur
	     Optional<AccountModel> accountOpt = accountService.getAccountByUser(user);
	
	     // Si le compte est trouvé, retourne-le ; sinon, retourne 404 Not Found
	     return accountOpt
	             .map(account -> ResponseEntity.ok().body(account))
	             .orElseGet(() -> ResponseEntity.notFound().build());
	 }
	
	 /**
	  * Endpoint POST /account/credit
	  * Permet de créditer le compte de l’utilisateur connecté avec un montant donné.
	  *
	  * @param amount Montant à créditer passé en paramètre de la requête
	  * @param principal Utilisateur connecté (automatiquement injecté)
	  */
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
	
	 /**
	  * Endpoint POST /account/debit
	  * Permet de débiter le compte de l’utilisateur connecté avec un montant donné.
	  *
	  * @param amount Montant à débiter passé en paramètre de la requête
	  * @param principal Utilisateur connecté (automatiquement injecté)
	  */
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