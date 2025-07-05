package com.openclassrooms.paymybuddy.controllerTest;

import com.openclassrooms.paymybuddy.controller.UserController;
import com.openclassrooms.paymybuddy.dto.UserDTO;
import com.openclassrooms.paymybuddy.dto.UserResponseDTO;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController();

	    @BeforeEach
	    public void setup() {
	        // Injection du mock dans le contrôleur
	        ReflectionTestUtils.setField(userController, "userService", userService);
	    }
	
	    @Test
	    public void testRegister() {
	        UserModel user = new UserModel();
	        user.setEmail("test@example.com");
	
	        when(userService.registerUser("test@example.com", "1234")).thenReturn(user);
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("test@example.com");
	        dto.setPassword("1234");
	
	        UserResponseDTO result = userController.register(dto);
	
	        assertNotNull(result);
	        assertEquals("test@example.com", result.getEmail());
	    }
	
	    @Test
	    public void testFindUser_Success() {
	        UserModel user = new UserModel();
	        user.setEmail("test@example.com");
	        //CORRECTION APRES ORAL AJOUT SEULEMENT DE DEUX DECIMAL
	        user.setBalance(BigDecimal.valueOf(100.0));

	        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

	        ResponseEntity<?> response = userController.findUser("test@example.com");

	        assertEquals(HttpStatus.OK, response.getStatusCode());

	        UserResponseDTO actual = (UserResponseDTO) response.getBody();
	        assertEquals("test@example.com", actual.getEmail());
	        assertEquals(100.0, actual.getBalance());
	    }
	
	    @Test
	    public void testFindUser_NotFound() {
	        when(userService.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
	
	        ResponseEntity<?> response = userController.findUser("unknown@example.com");
	
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertTrue(response.getBody().toString().contains("Utilisateur introuvable"));
	    }
	
	    @Test
	    public void testAddConnection_Success() {
	        UserModel user = new UserModel();
	        UserModel friend = new UserModel();
	
	        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
	        when(userService.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("user@example.com");
	        dto.setFriendEmail("friend@example.com");
	
	        String result = userController.addConnection(dto);
	
	        assertEquals("Connexion ajoutée", result);
	    }
	
	    @Test
	    public void testAddConnection_Failure() {
	        when(userService.findByEmail("user@example.com")).thenReturn(Optional.empty());
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("user@example.com");
	        dto.setFriendEmail("friend@example.com");
	
	        String result = userController.addConnection(dto);
	
	        assertEquals("Utilisateur ou ami introuvable", result);
	    }
	
	    @Test
	    public void testDeposit_Success() {
	        UserModel user = new UserModel();
	
	        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("test@example.com");
	        dto.setAmount(100.0);
	
	        String result = userController.deposit(dto);
	
	        assertEquals("Dépôt effectué", result);
	    }
	
	    @Test
	    public void testWithdraw_Success() {
	        UserModel user = new UserModel();
	
	        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("test@example.com");
	        dto.setAmount(50.0);
	
	        String result = userController.withdraw(dto);
	
	        assertEquals("Retrait effectué", result);
	    }
	
	    @Test
	    public void testWithdraw_InsufficientFunds() {
	        // Arrange
	        UserModel user = new UserModel();
	        user.setEmail("test@example.com");
	        //CORRECTION APRES ORAL AJOUT SEULEMENT DE DEUX DECIMAL
	        user.setBalance(BigDecimal.valueOf(100.0));

	        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	        doThrow(new IllegalArgumentException("Solde insuffisant"))
	            .when(userService).withdrawFunds(user, 200.0);

	        UserDTO dto = new UserDTO();
	        dto.setEmail("test@example.com");
	        dto.setAmount(200.0);

	        // Act
	        String result = userController.withdraw(dto);

	        // Assert
	        assertEquals("Solde insuffisant", result);
	    }
	
	    @Test
	    public void testDeposit_UserNotFound() {
	        when(userService.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());
	
	        UserDTO dto = new UserDTO();
	        dto.setEmail("inconnu@example.com");
	        dto.setAmount(100.0);
	
	        String result = userController.deposit(dto);
	
	        assertEquals("Utilisateur introuvable", result);
	    }
	}