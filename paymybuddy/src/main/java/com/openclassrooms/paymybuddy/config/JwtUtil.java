package com.openclassrooms.paymybuddy.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète pour signer le token (à garder confidentielle)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Durée de validité du token (ex : 24 heures)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Génère un token pour un utilisateur
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Le sujet du token est l'e-mail
                .setIssuedAt(new Date()) // Date d’émission
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration
                .signWith(null, key) // Signature avec la clé secrète
                .compact(); // Compacte en chaîne de caractères
    }
}