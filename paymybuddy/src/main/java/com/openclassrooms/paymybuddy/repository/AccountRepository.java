package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Bean Spring de type repository
public interface AccountRepository extends JpaRepository<AccountModel, Integer> {
	// Méthode pour recherche un compte à partir d’un utilisateur
    // Spring va automatiquement générer la requête SQL en fonction du nom de la méthode
    Optional<AccountModel> findByUser(UserModel user);
}