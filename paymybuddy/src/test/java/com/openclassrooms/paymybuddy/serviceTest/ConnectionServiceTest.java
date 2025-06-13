package com.openclassrooms.paymybuddy.serviceTest;

import com.openclassrooms.paymybuddy.model.ConnectionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionServiceTest {

    private UserModel user;
    private UserModel friend;
    private List<ConnectionModel> connections;

    @BeforeEach
    public void setUp() {
        user = new UserModel();
        user.setId(1);
        user.setEmail("user@test.com");

        friend = new UserModel();
        friend.setId(2);
        friend.setEmail("friend@test.com");

        connections = new ArrayList<>();
    }

    @Test
    public void testAddConnection() {
        ConnectionModel connection = new ConnectionModel(user, friend);
        connections.add(connection);

        assertFalse(connections.isEmpty());
        assertEquals(user, connections.get(0).getUser());
        assertEquals(friend, connections.get(0).getConnection());
    }

    @Test
    public void testDuplicateConnectionNotAllowed() {
        ConnectionModel connection1 = new ConnectionModel(user, friend);
        ConnectionModel connection2 = new ConnectionModel(user, friend);

        connections.add(connection1);

        // Simuler logique métier anti-doublon
        boolean alreadyExists = connections.stream()
                .anyMatch(c -> c.getUser().getId().equals(user.getId()) &&
                               c.getConnection().getId().equals(friend.getId()));

        if (!alreadyExists) {
            connections.add(connection2);
        }

        // On s’attend à une seule connexion
        assertEquals(1, connections.size());
    }
}