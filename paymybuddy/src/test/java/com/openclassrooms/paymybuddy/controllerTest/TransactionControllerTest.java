package com.openclassrooms.paymybuddy.controllerTest;

import com.openclassrooms.paymybuddy.controller.TransactionController;
import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TransactionControllerTest {

    private final UserService userService = mock(UserService.class);
    private final TransactionService transactionService = mock(TransactionService.class);
    private final TransactionController transactionController = new TransactionController();

    @BeforeEach
    public void setup() {
        // Injecter les services mockés
    	// Injection du mock dans le champ privé "connectionService"
        ReflectionTestUtils.setField(transactionController, "userService", userService);
        ReflectionTestUtils.setField(transactionController, "transactionService", transactionService);
    }

    @Test
    public void testTransfer_Success() {
        UserModel sender = new UserModel();
        sender.setEmail("sender@test.com");
        UserModel receiver = new UserModel();
        receiver.setEmail("receiver@test.com");

        // Mocking
        when(userService.findByEmail("sender@test.com")).thenReturn(Optional.of(sender));
        when(userService.findByEmail("receiver@test.com")).thenReturn(Optional.of(receiver));

        TransactionModel transaction = new TransactionModel();
        transaction.setAmount(50.0);
        transaction.setFee(0.25);

        when(transactionService.transfer(sender, receiver, 50.0, "Test")).thenReturn(transaction);

        // Créer le DTO
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderMail("sender@test.com");
        dto.setReceiverMail("receiver@test.com");
        dto.setAmount(50.0);
        dto.setDescription("Test");

        // Appel réel
        ResponseEntity<String> response = transactionController.transfer(dto);

        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Transaction réussie"));
    }

    @Test
    public void testTransfer_UserNotFound() {
        // Mock : expéditeur introuvable
        when(userService.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderMail("unknown@test.com");
        dto.setReceiverMail("receiver@test.com");
        dto.setAmount(50.0);
        dto.setDescription("Test");

        ResponseEntity<String> response = transactionController.transfer(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("introuvable"));
    }
}