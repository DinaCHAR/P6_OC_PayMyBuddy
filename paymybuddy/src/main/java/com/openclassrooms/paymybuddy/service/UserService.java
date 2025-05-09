package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.UserModel;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel createUser(String username, String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        UserModel user = new UserModel(username, email, hashedPassword);
        return userRepository.save(user);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean checkPassword(String email, String rawPassword) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(user ->
            passwordEncoder.matches(rawPassword, user.getPassword())
        ).orElse(false);
    }
}

