package com.openclassrooms.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//Indique que cette classe contient des beans de configuration Spring
@Configuration

//Active la configuration de sécurité Web avec Spring Security
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Déclare un bean BCryptPasswordEncoder. Ce composant sera utilisé pour encoder
	 * ou vérifier les mots de passe dans l'application, notamment lors de
	 * l'inscription ou de la connexion.
	 */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configure la chaîne de filtres de sécurité utilisée par Spring Security. Elle
	 * définit les règles d'accès, désactive la protection CSRF, et supprime le
	 * formulaire de login par défaut.
	 *
	 * @param http Objet HttpSecurity fourni par Spring pour personnaliser la
	 *             sécurité.
	 * @return un SecurityFilterChain configuré.
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Désactive la protection CSRF (Cross-Site Request Forgery)
				// utile lors de tests d'API sans interface HTML (comme avec Postman).
				.csrf(csrf -> csrf.disable())

				// Autorise certaines URL sans authentification
				.authorizeHttpRequests(auth -> auth
						// Autorise librement les routes d'inscription et de connexion utilisateur
						.requestMatchers(
					            "/api/users/register",
					            "/api/users/find",
					            "/api/users/add-connection",
					            "/api/users/deposit",
					            "/api/users/withdraw",
					            "/api/transactions/transfer",
					            "/api/transactions/user",
					            "/api/transactions/all",
					            "/api/account/**",
					            "/api/connections/**",
					            "/login", 
					            "/profile",
					            "/register",
					            "/profile/edit", 
					            "/profile/save",
					            "/profile/**",
					            "/add-connection",
					            "/add-connection/**",
					            "/connections",
					            "/dashboard",
					            "/dashboard/**",
					            "/transfer",
					            "/css/**",
					            "/js/**", 
					            "/images/**",
					            "/rest/transfer",
					            "/dashboard/transfer"
					       ).permitAll()

						// Toute autre requête doit être authentifiée
						.anyRequest().authenticated())

				// Désactive le formulaire de connexion par défaut fourni par Spring Security
				// Cela permet de gérer l'authentification manuellement via un contrôleur
				.formLogin(login -> login.disable());

		// Construit et retourne la configuration de sécurité
		return http.build();
	}
}
