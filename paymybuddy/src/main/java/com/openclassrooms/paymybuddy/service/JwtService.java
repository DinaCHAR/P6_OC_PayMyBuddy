package com.openclassrooms.paymybuddy.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

	// Injection de la clé secrète depuis le fichier de configuration
    @Value("${jwt.secret}")
    private String secretKey;

    // Durée de validité du token (en millisecondes)
    private final long validityInMilliseconds = 3600000; // 1 heure

    /**
     * Génère un token JWT pour un utilisateur à partir de son email
     *
     * @param email : L'email de l'utilisateur pour lequel le token est généré
     * @return String : Le token JWT généré
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // Le "subject" du token est l'email de l'utilisateur
                .setIssuedAt(new Date())  // Date de génération du token
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds)) // Date d'expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Utilisation de l'algorithme HMAC avec la clé secrète
                .compact();
    }

    /**
     * Extrait les informations des claims du token JWT
     *
     * @param token : Le token JWT à décoder
     * @return Claims : Les informations extraites du token (ici, l'email de l'utilisateur)
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)  // Clé secrète utilisée pour vérifier la signature
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Récupère l'email du token JWT
     *
     * @param token : Le token JWT
     * @return String : L'email de l'utilisateur
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();  // Retourne l'email stocké dans le "subject"
    }

    /**
     * Vérifie si le token est encore valide (pas expiré)
     *
     * @param token : Le token JWT
     * @return boolean : Vrai si le token est valide, sinon faux
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());  // Vérifie si la date d'expiration est passée
    }

    /**
     * Valide un token en vérifiant son intégrité et son expiration
     *
     * @param token : Le token JWT à valider
     * @param email : L'email de l'utilisateur à comparer avec le token
     * @return boolean : Vrai si le token est valide, sinon faux
     */
    public boolean validateToken(String token, String email) {
        return (email.equals(extractEmail(token)) && !isTokenExpired(token));
    }
}
