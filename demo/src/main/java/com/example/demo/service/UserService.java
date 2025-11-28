package com.example.demo.service;

import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        // Verificar si el username ya existe
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el nuevo username ya existe (si ha cambiado)
        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setLevel(userDetails.getLevel());
        user.setExperience(userDetails.getExperience());
        user.setWins(userDetails.getWins());
        user.setLosses(userDetails.getLosses());
        user.setDraws(userDetails.getDraws());

        return userRepository.save(user);
    }

    public boolean updateUsername(String userId, String newUsername) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Verificar si el nuevo username ya existe
            if (userRepository.existsByUsername(newUsername) &&
                    !user.getUsername().equals(newUsername)) {
                return false;
            }

            user.setUsername(newUsername);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}