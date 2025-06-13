package com.openclassrooms.paymybuddy.serviceTest;

import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.service.AccountService;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService();
        ReflectionTestUtils.setField(accountService, "accountRepository", accountRepository);
    }

    @Test
    public void testCreditAccount() {
        UserModel user = new UserModel();
        AccountModel account = new AccountModel();
        account.setUser(user);
        account.setBalance(100.0);

        when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountModel.class))).thenAnswer(i -> i.getArgument(0));

        accountService.creditAccount(user, 50.0);

        assertEquals(150.0, account.getBalance());
    }

    @Test
    public void testDebitAccount() {
        UserModel user = new UserModel();
        AccountModel account = new AccountModel();
        account.setUser(user);
        account.setBalance(100.0);

        when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountModel.class))).thenAnswer(i -> i.getArgument(0));

        accountService.debitAccount(user, 30.0);

        assertEquals(70.0, account.getBalance());
    }

    @Test
    public void testDebitAccount_InsufficientFunds() {
        UserModel user = new UserModel();
        AccountModel account = new AccountModel();
        account.setUser(user);
        account.setBalance(10.0);

        when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.debitAccount(user, 20.0);
        });

        assertEquals("Solde insuffisant.", exception.getMessage());
    }
}