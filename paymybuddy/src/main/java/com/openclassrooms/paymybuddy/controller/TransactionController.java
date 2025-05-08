package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//Indique que cette classe est un contrôleur REST (retourne des objets JSON)
@RestController

//Préfixe commun à toutes les routes de ce contrôleur
@RequestMapping("/api/transactions")
public class TransactionController {

	 // Injection du service qui contient la logique métier pour les transactions
	 @Autowired
	 private TransactionService transactionService;
	
	 /**
	  * Endpoint POST /api/transactions/create
	  * Permet de créer une nouvelle transaction entre deux utilisateurs.
	  *
	  * @param transactionDetails Map contenant senderEmail, receiverEmail, amount, description, date
	  * @return La transaction créée ou une erreur si la création échoue
	  */
	 @PostMapping("/create")
	 public ResponseEntity<TransactionModel> createTransaction(@RequestBody Map<String, Object> transactionDetails) {
	     try {
	         // Extraction des informations de la requête
	         String senderEmail = (String) transactionDetails.get("senderEmail");
	         String receiverEmail = (String) transactionDetails.get("receiverEmail");
	         Double amount = Double.valueOf(transactionDetails.get("amount").toString());
	         String description = (String) transactionDetails.get("description");
	         String date = (String) transactionDetails.get("date");
	
	         // Appel du service pour créer une transaction
	         TransactionModel transaction = transactionService.createTransaction(
	             senderEmail, receiverEmail, amount, description, date
	         );
	
	         // Retourne la transaction avec un statut HTTP 201 (Created)
	         return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	     } catch (Exception e) {
	         // En cas d'erreur, retourne un statut HTTP 400 (Bad Request)
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	     }
	 }
	
	 /**
	  * Endpoint GET /api/transactions/user/{email}
	  * Permet de récupérer toutes les transactions liées à un utilisateur.
	  *
	  * @param email Email de l’utilisateur
	  * @return Liste des transactions ou une erreur si l'utilisateur n'existe pas
	  */
	 @GetMapping("/user/{email}")
	 public ResponseEntity<List<TransactionModel>> getTransactionsByUser(@PathVariable String email) {
	     try {
	         // Appel du service pour récupérer les transactions de l'utilisateur
	         List<TransactionModel> transactions = transactionService.getUserTransactions(email);
	         return ResponseEntity.ok(transactions);
	     } catch (Exception e) {
	         // En cas d'erreur, retourne un statut HTTP 404 (Not Found)
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 }
	}