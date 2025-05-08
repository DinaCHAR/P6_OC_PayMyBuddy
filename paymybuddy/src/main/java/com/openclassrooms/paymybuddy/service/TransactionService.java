package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Effectuer une nouvelle transaction entre un utilisateur et un autre
    public TransactionModel createTransaction(String senderEmail, String receiverEmail, Double amount, String description, String date) {
        // Recherche des utilisateurs par email
        UserModel sender = userRepository.findByEmail(senderEmail).orElse(null);
        UserModel receiver = userRepository.findByEmail(receiverEmail).orElse(null);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("L'un des utilisateurs n'existe pas");
        }

        // Créer et sauvegarder la transaction
        TransactionModel transaction = new TransactionModel(sender, receiver, amount, description, date);
        return transactionRepository.save(transaction);
    }

    // Récupérer toutes les transactions d'un utilisateur
    public List<TransactionModel> getUserTransactions(String userEmail) {
        UserModel user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }

        // Retourne toutes les transactions où l'utilisateur est soit l'expéditeur, soit le destinataire
        return transactionRepository.findBySenderOrReceiver(user, user);
    }
}
