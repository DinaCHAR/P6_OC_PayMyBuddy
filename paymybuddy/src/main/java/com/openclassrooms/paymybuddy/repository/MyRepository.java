package com.openclassrooms.paymybuddy.repository;

import org.springframework.boot.SpringApplication;

import com.openclassrooms.paymybuddy.PaymybuddyApplication;

public class MyRepository {
	
	// Méthode principale pour démarrer l'application
		public static void main(String[] args) {
		    // Démarrer l'application Spring Boot en utilisant la classe SafetyNetApplication
			SpringApplication.run(PaymybuddyApplication.class, args);

		    // Création d'une instance de myRepo et appel de la méthode Init pour charger les données depuis le fichier JSON
		    MyRepository repo = new MyRepository();
		    repo.Init();
		}

		public void Init() {
			// TODO Auto-generated method stub
			
		}
		

}
