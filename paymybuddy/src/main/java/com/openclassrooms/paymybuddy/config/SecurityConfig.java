package com.openclassrooms.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Déclare un encoder pour pouvoir l’injecter ailleurs
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Définit les règles de sécurité
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // pour tests API sans souci de token CSRF
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login.disable()); // on gère l’authentification nous-mêmes

        return http.build();
    }
}
