package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.BankAccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Indique que cette classe est un contrôleur REST (les réponses seront automatiquement formatées en JSON)
@RestController

//Définit le préfixe commun à toutes les routes de ce contrôleur
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

	 // Injection automatique du service de gestion des comptes bancaires
	 @Autowired
	 private BankAccountService bankAccountService;
	
	 // Injection automatique du service utilisateur pour accéder aux informations d’un utilisateur
	 @Autowired
	 private UserService userService;
	
	 /**
	  * Endpoint POST /api/bank-accounts
	  * Permet d’ajouter un nouveau compte bancaire.
	  * 
	  * @param account Le compte bancaire à ajouter (fourni dans le corps de la requête JSON)
	  * @return Le compte sauvegardé avec un code 200 (OK)
	  */
	 @PostMapping
	 public ResponseEntity<BankAccountModel> addBankAccount(@RequestBody BankAccountModel account) {
	     // Enregistre le compte via le service
	     BankAccountModel saved = bankAccountService.addBankAccount(account);
	     return ResponseEntity.ok(saved); // Retourne le compte sauvegardé
	 }
	
	 /**
	  * Endpoint GET /api/bank-accounts/user?email=...
	  * Permet de récupérer tous les comptes bancaires associés à un utilisateur via son email.
	  * 
	  * @param email L’email de l’utilisateur pour lequel on veut récupérer les comptes
	  * @return La liste des comptes bancaires liés à l’utilisateur
	  */
	 @GetMapping("/user")
	 public ResponseEntity<List<BankAccountModel>> getAccountsByUserEmail(@RequestParam String email) {
	     // Recherche l’utilisateur correspondant à l’email
	     UserModel user = userService.findByEmail(email);
	
	     // Récupère tous les comptes liés à cet utilisateur
	     List<BankAccountModel> accounts = bankAccountService.getBankAccountsByUser(user);
	
	     // Retourne la liste des comptes avec un code 200 (OK)
	     return ResponseEntity.ok(accounts);
	 }
	}
