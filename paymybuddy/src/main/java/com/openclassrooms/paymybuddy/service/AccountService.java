package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // Indique que c’est un service métier géré par Spring
@Transactional
public class AccountService {

    @Autowired // Injection automatique du repository
    private AccountRepository accountRepository;

    //Récupérer un compte via un utilisateur
    public Optional<AccountModel> getAccountByUser(UserModel user) {
        return accountRepository.findByUser(user);
    }

    //  Crédite un compte (ajoute de l’argent au solde)
    public void creditAccount(UserModel user, double amount) {
    	AccountModel account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        account.credit(amount); // méthode définie dans ta classe Account
        accountRepository.save(account); // JPA met à jour le compte en BDD
    }

    // Débite un compte (retire de l’argent)
    public void debitAccount(UserModel user, double amount) {
    	AccountModel account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // La méthode debit vérifie le solde, et lève une exception si insuffisant
        account.debit(amount);
        accountRepository.save(account);
    }

    // Crée un compte vide pour un utilisateur (au moment de l’inscription par exemple)
    public AccountModel createAccountForUser(UserModel user) {
    	AccountModel account = new AccountModel(user); // solde initial = 0
        return accountRepository.save(account); // sauvegarde en BDD
    }
}