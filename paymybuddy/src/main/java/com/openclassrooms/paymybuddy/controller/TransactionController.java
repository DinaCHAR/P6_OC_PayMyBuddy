package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

//Cette classe est un contrôleur REST (retourne des données JSON)
@RestController

//Préfixe de toutes les routes définies dans ce contrôleur : /api/transactions
@RequestMapping("/api/transactions")
public class TransactionController {

 // Injection du service de transaction
 @Autowired
 private TransactionService transactionService;

 // Injection du service utilisateur
 @Autowired
 private UserService userService;

	 /**
	  * Effectue une transaction entre deux utilisateurs.
	  * Requête POST vers /api/transactions/transfer
	  * Reçoit un TransactionDTO dans le corps de la requête.
	  */
	 @PostMapping("/transfer")
	 public ResponseEntity<String> transfer(@RequestBody TransactionDTO dto) {
	
	     Optional<UserModel> senderOpt = userService.findByEmail(dto.getSenderMail());
	     Optional<UserModel> receiverOpt = userService.findByEmail(dto.getReceiverMail());
	
	     if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
	         return ResponseEntity.badRequest().body("Utilisateur émetteur ou destinataire introuvable.");
	     }
	
	     try {
	         TransactionModel t = transactionService.transfer(
	             senderOpt.get(),
	             receiverOpt.get(),
	             dto.getAmount(),
	             dto.getDescription()
	         );
	
	         return ResponseEntity.ok(
	             "Transaction réussie : " + t.getAmount() +
	             " € envoyés avec une commission de " + t.getFee() + " €."
	         );
	     } catch (IllegalArgumentException e) {
	         return ResponseEntity.badRequest().body(e.getMessage());
	     }
	 }
	
	 /**
	  * Récupère toutes les transactions enregistrées.
	  * Requête GET vers /api/transactions/all
	  */
	 @GetMapping("/all")
	 public ResponseEntity<?> all() {
	     return ResponseEntity.ok(transactionService.getAllTransactions());
	 }
	}