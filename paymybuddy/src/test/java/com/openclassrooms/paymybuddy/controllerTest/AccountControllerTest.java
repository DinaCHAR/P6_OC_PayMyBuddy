package com.openclassrooms.paymybuddy.controllerTest;

import com.openclassrooms.paymybuddy.controller.AccountController;
import com.openclassrooms.paymybuddy.dto.AccountDTO;
import com.openclassrooms.paymybuddy.model.AccountModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.AccountService;
import com.openclassrooms.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private final AccountService accountService = mock(AccountService.class);
    private final UserService userService = mock(UserService.class);
    private final AccountController accountController = new AccountController();

    @BeforeEach
    public void setup() {
        // Injection manuelle des mocks
        ReflectionTestUtils.setField(accountController, "accountService", accountService);
        ReflectionTestUtils.setField(accountController, "userService", userService);
    }

    @Test
    public void testGetAccount_Success() {
        UserModel user = new UserModel();
        AccountModel account = new AccountModel();

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(accountService.getAccountByUser(user)).thenReturn(Optional.of(account));

        ResponseEntity<?> response = accountController.getAccount("test@test.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCredit_Success() {
        UserModel user = new UserModel();

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        AccountDTO dto = new AccountDTO();
        dto.setEmail("test@test.com");
        dto.setAmount(100.0);

        ResponseEntity<String> response = accountController.credit(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Compte crédité"));
    }

    @Test
    public void testDebit_Success() {
        UserModel user = new UserModel();

        when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        AccountDTO dto = new AccountDTO();
        dto.setEmail("test@test.com");
        dto.setAmount(50.0);

        ResponseEntity<String> response = accountController.debit(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Compte débité"));
    }
}