package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indique que c’est un composant Spring pour accéder aux données
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    // Méthode personnalisée : chercher un utilisateur par son email
    Optional<UserModel> findByEmail(String email);
}
