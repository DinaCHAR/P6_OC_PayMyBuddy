package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public TransactionModel transfer(UserModel sender, UserModel receiver, double amount, String description) {
        double fee = amount * 0.005;
        double total = amount + fee;

        if (sender.getBalance() < total) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer la transaction.");
        }

        sender.setBalance(sender.getBalance() - total);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        TransactionModel transaction = new TransactionModel();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setFee(fee);
        transaction.setDescription(description);
        transaction.setDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

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
}