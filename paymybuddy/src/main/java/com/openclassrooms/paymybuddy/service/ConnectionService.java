package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.ConnectionModel;
import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.ConnectionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indique que c’est une classe de service métier
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

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

    // Ajouter une connexion entre deux utilisateurs
    public boolean addConnection(String userEmail, String connectionEmail) {
        // Recherche des deux utilisateurs par leur email
        Optional<UserModel> optionalUser = userRepository.findByEmail(userEmail);
        Optional<UserModel> optionalConnection = userRepository.findByEmail(connectionEmail);

        // Vérification si les utilisateurs existent et ne sont pas égaux
        if (optionalUser.isPresent() && optionalConnection.isPresent()) {
            UserModel user = optionalUser.get();
            UserModel connection = optionalConnection.get();

            if (user.equals(connection)) {
                return false; // Ne pas connecter un utilisateur à lui-même
            }

            // Vérifier si la connexion existe déjà
            if (connectionRepository.existsByUserAndConnection(user, connection)) {
                return false; // Connexion déjà existante
            }

            // Créer une nouvelle connexion et la sauvegarder
            ConnectionModel newConnection = new ConnectionModel(user, connection);
            connectionRepository.save(newConnection);
            return true;
        }

        return false; // Si l'un des utilisateurs n'existe pas, retourner false
    }
}
