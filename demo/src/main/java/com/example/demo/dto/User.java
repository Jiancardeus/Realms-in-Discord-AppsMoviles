package com.example.demo.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "El username es obligatorio")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private int level = 1;
    private int experience = 0;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}