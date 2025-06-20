package com.openclassrooms.paymybuddy.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.paymybuddy.controller.TransactionController;
import com.openclassrooms.paymybuddy.dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.TransactionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional // pour rollback auto après chaque test
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Créer deux utilisateurs
        UserModel sender = new UserModel();
        sender.setEmail("sender@test.com");
        sender.setPassword("pass");
        sender.setBalance(100.0);
        userRepository.save(sender);

        UserModel receiver = new UserModel();
        receiver.setEmail("receiver@test.com");
        receiver.setPassword("pass");
        userRepository.save(receiver);
    }

    @Test
    public void testTransfer_Success() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderMail("sender@test.com");
        dto.setReceiverMail("receiver@test.com");
        dto.setAmount(50.0);
        dto.setDescription("Test Transfer");

        mockMvc.perform(post("/api/transactions/rest/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard?email=sender@test.com"));

        // Vérifie que la transaction est bien en base
        List<TransactionModel> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        assertEquals(50.0, transactions.get(0).getAmount());
    }

    @Test
    public void testTransfer_UserNotFound() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderMail("unknown@test.com");
        dto.setReceiverMail("receiver@test.com");
        dto.setAmount(30.0);
        dto.setDescription("Fail test");

        mockMvc.perform(post("/api/transactions/rest/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Vérifie qu’aucune transaction n’a été créée
        assertTrue(transactionRepository.findAll().isEmpty());
    }
}