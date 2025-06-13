package com.openclassrooms.paymybuddy.serviceTest;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserService userService = new UserService();

    @BeforeEach
    public void setUp() {
        // Injecte le mock dans le service
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    public void testFindByEmail_UserExists() {
        String email = "test@example.com";
        UserModel user = new UserModel();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserModel> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    public void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<UserModel> result = userService.findByEmail("unknown@example.com");

        assertFalse(result.isPresent());
    }
}