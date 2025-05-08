package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.ConnectionModel;
import com.openclassrooms.paymybuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//Indique que cette classe est un contrôleur REST qui retourne des objets JSON
@RestController

//Définit le préfixe d’URL pour toutes les routes de ce contrôleur
@RequestMapping("/api/connections")
public class ConnectionController {

	 // Injection du service de gestion des connexions
	 @Autowired
	 private ConnectionService connectionService;
	
	 /**
	  * Endpoint POST /api/connections/add
	  * Permet d’ajouter une connexion entre deux utilisateurs.
	  * 
	  * @param emails Map contenant les clés "userEmail" et "connectionEmail"
	  * @return Message de succès ou d’échec
	  */
	 @PostMapping("/add")
	 public ResponseEntity<String> addConnection(@RequestBody Map<String, String> emails) {
	     String userEmail = emails.get("userEmail");
	     String connectionEmail = emails.get("connectionEmail");
	
	     // Appel du service pour ajouter la connexion
	     if (connectionService.addConnection(userEmail, connectionEmail)) {
	         return ResponseEntity.ok("Connexion ajoutée !");
	     } else {
	         return ResponseEntity.badRequest().body("Erreur lors de l’ajout de la connexion.");
	     }
	 }
	
	 /**
	  * Endpoint GET /api/connections?email=...
	  * Récupère la liste des connexions d’un utilisateur donné.
	  * 
	  * @param email Email de l’utilisateur dont on veut les connexions
	  * @return Liste des connexions liées à cet utilisateur
	  */
	 @GetMapping
	 public ResponseEntity<List<ConnectionModel>> getConnections(@RequestParam String email) {
	     // Appel du service pour récupérer les connexions
	     List<ConnectionModel> connections = connectionService.getConnections(email);
	     return ResponseEntity.ok(connections);
	 }
	}
