package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.ConnectionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionModel, Integer> {

    // Récupérer toutes les connexions d’un utilisateur
    List<ConnectionModel> findByUser(UserModel user);

    // Vérifier si une connexion existe déjà
    boolean existsByUserAndConnection(UserModel user, UserModel connection);
}
