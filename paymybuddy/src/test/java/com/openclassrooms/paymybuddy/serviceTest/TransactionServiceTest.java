package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        transactionRepository = mock(TransactionRepository.class);
        userRepository = mock(UserRepository.class);

        transactionService = new TransactionService();
        org.springframework.test.util.ReflectionTestUtils.setField(transactionService, "transactionRepository", transactionRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(transactionService, "userRepository", userRepository);
    }

    @Test
    public void testTransferFeeCalculation() {
        // Arrange
        UserModel sender = new UserModel();
        sender.setEmail("sender@test.com");
        sender.setBalance(100.0);

        UserModel receiver = new UserModel();
        receiver.setEmail("receiver@test.com");
        receiver.setBalance(0.0);

        // Simuler les sauvegardes
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(TransactionModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TransactionModel t = transactionService.transfer(sender, receiver, 50.0, "Test");

        // Assert
        assertEquals(50.0, t.getAmount());
        assertEquals(0.25, t.getFee()); // 0.5% fee
        assertEquals("Test", t.getDescription());
        assertEquals("sender@test.com", t.getSender().getEmail());
        assertEquals("receiver@test.com", t.getReceiver().getEmail());
        assertNotNull(t.getDate());

        // Et vérifier que les balances ont été mises à jour
        assertEquals(49.75, sender.getBalance());
        assertEquals(50.0, receiver.getBalance());
    }
}