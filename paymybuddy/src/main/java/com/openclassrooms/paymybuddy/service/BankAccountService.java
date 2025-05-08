package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankAccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Indique que cette classe est un service Spring, utilisée pour contenir la logique métier de l'application.
public class BankAccountService {

    // Injection de dépendance du repository pour accéder aux opérations de la base de données concernant les comptes bancaires
    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * Cette méthode permet d'ajouter un nouveau compte bancaire dans la base de données.
     * @param account Le compte bancaire à ajouter.
     * @return Le compte bancaire enregistré, tel qu'il est retourné par le repository après la persistance.
     */
    public BankAccountModel addBankAccount(BankAccountModel account) {
        // Enregistrement du compte bancaire via le repository
        return bankAccountRepository.save(account);
    }

    /**
     * Cette méthode permet de récupérer tous les comptes bancaires associés à un utilisateur spécifique.
     * @param user L'utilisateur pour lequel nous cherchons les comptes bancaires associés.
     * @return Une liste de comptes bancaires associés à l'utilisateur donné.
     */
    public List<BankAccountModel> getBankAccountsByUser(UserModel user) {
        // Appel au repository pour récupérer les comptes bancaires de l'utilisateur
        return bankAccountRepository.findByUser(user);
    }
}

