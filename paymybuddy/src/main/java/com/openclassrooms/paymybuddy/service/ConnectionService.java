package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.ConnectionDTO;
import com.openclassrooms.paymybuddy.model.ConnectionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.ConnectionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Indique que c’est une classe de service métier
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    // Récupérer les connexions d'un utilisateur par son email
    public List<ConnectionModel> getConnections(String userEmail) {
        // Recherche de l'utilisateur par email avec Optional
        Optional<UserModel> optionalUser = userRepository.findByEmail(userEmail);

        // Si l'utilisateur existe, on retourne ses connexions
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            return connectionRepository.findByUser(user);
        } else {
            return null; // Si l'utilisateur n'existe pas, retour null ou gérer autrement
        }
    }

    public boolean addConnection(String userEmail, String connectionEmail) {
        // Empêche les emails vides ou null
        if (userEmail == null || connectionEmail == null || userEmail.isBlank() || connectionEmail.isBlank()) {
            return false;
        }

        // Refuse la connexion à soi-même
        if (userEmail.equalsIgnoreCase(connectionEmail)) {
            return false;
        }

        Optional<UserModel> optionalUser = userRepository.findByEmail(userEmail);
        Optional<UserModel> optionalConnection = userRepository.findByEmail(connectionEmail);

        // Vérifie que les deux utilisateurs existent
        if (optionalUser.isEmpty() || optionalConnection.isEmpty()) {
            return false;
        }

        UserModel user = optionalUser.get();
        UserModel connection = optionalConnection.get();

        // Refuse une connexion déjà existante
        boolean alreadyExists = connectionRepository.existsByUserAndConnection(user, connection);
        if (alreadyExists) {
            return false;
        }

        // Crée et enregistre la nouvelle connexion
        ConnectionModel newConnection = new ConnectionModel(user, connection);
        connectionRepository.save(newConnection);

        return true;
    }

    // Récupère les connexions d'un utilisateur et les transforme en DTO
    public List<ConnectionDTO> getConnectionsDTO(String userEmail) {
        // Recherche de l'utilisateur à partir de son email
        Optional<UserModel> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) return new ArrayList<>(); // Retourne une liste vide si absent

        UserModel user = userOpt.get();

        // Récupération des connexions liées à l'utilisateur
        List<ConnectionModel> connections = connectionRepository.findByUser(user);

        // Transformation en objets DTO pour l'affichage ou le transfert vers la couche web
        return connections.stream()
            .map(conn -> new ConnectionDTO(user.getEmail(), conn.getConnection().getEmail()))
            .collect(Collectors.toList());
    }
}
