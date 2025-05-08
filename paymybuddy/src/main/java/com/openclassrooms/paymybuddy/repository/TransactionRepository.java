package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indique que c'est un repository pour l'accès aux données
public interface TransactionRepository extends JpaRepository<TransactionModel, Integer> {

    // Trouver les transactions par l'utilisateur qui a envoyé ou reçu de l'argent
    List<TransactionModel> findBySenderOrReceiver(UserModel sender, UserModel receiver);
}
