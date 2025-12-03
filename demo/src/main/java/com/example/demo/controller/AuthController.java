package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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
            System.out.println("🔔 [Spring] Received update-username request");
            System.out.println("📨 [Spring] Request body: " + request);

            String currentUsername = request.get("currentUsername"); //
            String newUsername = request.get("newUsername");

            System.out.println("👤 [Spring] currentUsername: " + currentUsername + ", newUsername: " + newUsername);

            if (currentUsername == null || newUsername == null) {
                System.out.println("❌ [Spring] Missing parameters");
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "currentUsername y newUsername son requeridos");
                return ResponseEntity.badRequest().body(response);
            }

            // Buscar usuario por username actual
            Optional<User> userOpt = userService.getUserByUsername(currentUsername);
            if (userOpt.isEmpty()) {
                System.out.println("❌ [Spring] User not found with username: " + currentUsername);
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            boolean success = userService.updateUsername(user.getId(), newUsername);

            if (success) {
                System.out.println("✅ [Spring] Username updated successfully");
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Username actualizado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                System.out.println("❌ [Spring] Username update failed");
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (RuntimeException e) {
            System.out.println("💥 [Spring] Exception: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        try {
            System.out.println("🔔 [Spring] Received delete user request");
            System.out.println("👤 [Spring] username to delete: " + username);

            if (username == null || username.isBlank()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "username es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            // Buscar usuario por username
            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                System.out.println("❌ [Spring] User not found: " + username);
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            userService.deleteUser(user.getId());

            System.out.println("✅ [Spring] User deleted successfully");
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("💥 [Spring] Exception: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
