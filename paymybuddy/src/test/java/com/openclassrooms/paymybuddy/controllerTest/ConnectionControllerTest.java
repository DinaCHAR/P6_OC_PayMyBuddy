package com.openclassrooms.paymybuddy.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.paymybuddy.controller.ConnectionController;
import com.openclassrooms.paymybuddy.dto.ConnectionDTO;
import com.openclassrooms.paymybuddy.service.ConnectionService;

public class ConnectionControllerTest {

    private final ConnectionService connectionService = mock(ConnectionService.class);
    private final ConnectionController connectionController = new ConnectionController();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(connectionController, "connectionService", connectionService);
    }

    @Test
    public void testAddConnection_Success() {
        ConnectionDTO dto = new ConnectionDTO("user@test.com", "friend@test.com");

        when(connectionService.addConnection("user@test.com", "friend@test.com")).thenReturn(true);

        ResponseEntity<String> response = connectionController.addConnection(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Connexion ajoutée !", response.getBody());
    }

    @Test
    public void testAddConnection_Failure() {
        ConnectionDTO dto = new ConnectionDTO("user@test.com", "friend@test.com");

        when(connectionService.addConnection("user@test.com", "friend@test.com")).thenReturn(false);

        ResponseEntity<String> response = connectionController.addConnection(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erreur lors de l’ajout de la connexion.", response.getBody());
    }
}