package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service // Indique que cette classe est un service Spring gérant la logique métier liée aux transactions
public class TransactionService {

    @Autowired // Injecte automatiquement l'implémentation de TransactionRepository
    private TransactionRepository transactionRepository;

    @Autowired // Injecte automatiquement l'implémentation de UserRepository
    private UserRepository userRepository;

    // Effectue un transfert d'argent entre deux utilisateurs
    //CORRECTION APRES ORAL/ AJOUT DE TRANSACTIONAL GERER LES TRANSACTIONS QUI INTERAGISSE AVEC LA BDD
    @Transactional
    public TransactionModel transfer(UserModel sender, UserModel receiver, double amount, String description) {
        BigDecimal amountBD = BigDecimal.valueOf(amount);
        BigDecimal fee = amountBD.multiply(BigDecimal.valueOf(0.005)).setScale(2, RoundingMode.HALF_UP); // Calcul des frais de transaction : 0,5%
        BigDecimal total = amountBD.add(fee); // Montant total à débiter, incluant les frais

        // Vérifie si l'expéditeur a assez de solde pour couvrir le montant + frais
        if (sender.getBalance().compareTo(total) < 0) { // CORRECTION APRES ORAL
            throw new IllegalArgumentException("Solde insuffisant pour effectuer la transaction.");
        }

        // Débit du total depuis le compte de l'expéditeur
        sender.setBalance(sender.getBalance().subtract(total)); // ERREUR corrigee ici
        // Crédit du montant (hors frais) sur le compte du destinataire
        receiver.setBalance(receiver.getBalance().add(amountBD));

        // Sauvegarde les nouveaux soldes des deux utilisateurs
        userRepository.save(sender);
        userRepository.save(receiver);

        // Création et enregistrement de l'objet TransactionModel (à compléter si nécessaire)
        TransactionModel transaction = new TransactionModel();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        //CORRECTION APRES ORAL 
        transaction.setAmount(amountBD.doubleValue());
        transaction.setFee(fee.doubleValue());
        transaction.setDescription(description);
        transaction.setDate(LocalDateTime.now()); // Date de la transaction

        return transactionRepository.save(transaction);
    }

    // Récupère toutes les transactions existantes et les convertit en DTOs
    public List<TransactionDTO> getAllTransactions() {
        List<TransactionModel> transactions = transactionRepository.findAll();
        List<TransactionDTO> dtos = new ArrayList<>();
        
        for (TransactionModel transaction : transactions) {
            TransactionDTO dto = new TransactionDTO();
            dto.setAmount(transaction.getAmount());
            dto.setId(transaction.getId());
            dto.setDate(transaction.getDate());
            dto.setFee(transaction.getFee());
            dto.setDescription(transaction.getDescription());
            dto.setSenderMail(transaction.getSender().getEmail());
            dto.setReceiverMail(transaction.getReceiver().getEmail());
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    // Récupère les transactions liées à un utilisateur spécifique via son e-mail
    public List<TransactionDTO> getTransactionsByUserEmail(String email) {
        Optional<UserModel> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>(); // Aucun utilisateur trouvé → retourne une liste vide
        }

        UserModel user = userOpt.get();

        List<TransactionModel> sent = user.getSentTransactions(); // Transactions envoyées
        List<TransactionModel> received = user.getReceivedTransactions(); // Transactions reçues

        List<TransactionDTO> dtos = new ArrayList<>();

        // Conversion des transactions envoyées en DTO
        for (TransactionModel t : sent) {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(t.getId());
            dto.setSenderMail(t.getSender().getEmail());
            dto.setReceiverMail(t.getReceiver().getEmail());
            dto.setAmount(t.getAmount());
            dto.setFee(t.getFee());
            dto.setDate(t.getDate());
            dto.setDescription(t.getDescription());
            dtos.add(dto);
        }

        // Conversion des transactions reçues en DTO
        for (TransactionModel t : received) {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(t.getId());
            dto.setSenderMail(t.getSender().getEmail());
            dto.setReceiverMail(t.getReceiver().getEmail());
            dto.setAmount(t.getAmount());
            dto.setFee(t.getFee());
            dto.setDate(t.getDate());
            dto.setDescription(t.getDescription());
            dtos.add(dto);
        }

        // Trie la liste par date décroissante (les plus récentes d'abord)
        dtos.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return dtos;
    }
}
