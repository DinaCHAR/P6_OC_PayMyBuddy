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

 // Ajouter une nouvelle connexion entre deux utilisateurs
    public boolean addConnection(String userEmail, String connectionEmail) {
        // Recherche des utilisateurs correspondant aux emails fournis
        Optional<UserModel> optionalUser = userRepository.findByEmail(userEmail);
        Optional<UserModel> optionalConnection = userRepository.findByEmail(connectionEmail);

        // Vérifie que les deux utilisateurs existent
        if (optionalUser.isPresent() && optionalConnection.isPresent()) {
            UserModel user = optionalUser.get();
            UserModel connection = optionalConnection.get();

            // Refuse la connexion à soi-même
            if (user.equals(connection)) return false;

            // Refuse une connexion déjà existante
            if (connectionRepository.existsByUserAndConnection(user, connection)) return false;

            // Crée et enregistre la nouvelle connexion
            connectionRepository.save(new ConnectionModel(user, connection));
            return true;
        }

        // Retourne false si l'un des deux utilisateurs n'existe pas
        return false;
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
