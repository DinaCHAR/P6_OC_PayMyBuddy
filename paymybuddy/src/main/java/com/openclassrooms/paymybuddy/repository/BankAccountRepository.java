package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.BankAccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Indique que cette interface est un composant Spring Data qui gère l'accès aux données dans la base de données
public interface BankAccountRepository extends JpaRepository<BankAccountModel, Integer> {

    // Cette méthode permet de récupérer la liste des comptes bancaires associés à un utilisateur spécifique.
    // Elle utilise une requête dérivée de Spring Data JPA qui recherche les comptes bancaires en fonction de l'utilisateur (user).
    List<BankAccountModel> findByUser(UserModel user);
}
