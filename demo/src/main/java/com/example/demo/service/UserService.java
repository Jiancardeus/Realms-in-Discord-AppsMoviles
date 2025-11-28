package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User dbUser = getById(id);
        dbUser.setName(user.getName());
        dbUser.setEmail(user.getEmail());
        return userRepository.save(dbUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
