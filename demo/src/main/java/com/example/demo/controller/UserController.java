package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();  // Cambiado de getAll() a getAllUsers()
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {  // Cambiado de Long a String
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);  // Cambiado de create() a createUser()
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody User user) {  // Cambiado de Long a String
        try {
            User updatedUser = userService.updateUser(id, user);  // Cambiado de update() a updateUser()
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {  // Cambiado de Long a String
        try {
            userService.deleteUser(id);  // Cambiado de delete() a deleteUser()
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}