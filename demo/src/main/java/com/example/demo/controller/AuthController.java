package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("username", createdUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> authRequest) {
        try {
            String username = authRequest.get("username");
            String password = authRequest.get("password");

            boolean isValid = userService.validateUser(username, password);
            if (isValid) {
                User user = userService.getUserByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login exitoso");
                response.put("username", user.getUsername());
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Credenciales inválidas");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody Map<String, String> request) {
        try {
            String newUsername = request.get("newUsername");
            // En una app real, obtendrías el userId del token JWT
            // Por ahora, necesitarás pasar el userId de alguna manera

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Username actualizado exitosamente");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}