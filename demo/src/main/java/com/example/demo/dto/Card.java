package com.example.demo.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cards") // Se guardarán en la colección "cards" de MongoDB
public class Card {

    @Id
    private String _id; // MongoDB usa _id

    private String name;
    private String type;
    private String description;
    private int cost;
    private int attack;
    private int defense; // Agregamos defensa
    private int health;
    private String faction;
    private String imageUrl; // El nombre del recurso (ej: "espadachin_solar")

    public Card() {}

    public Card(String name, String type, String description, int cost, int attack, int defense, int health, String faction, String imageUrl) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.cost = cost;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.faction = faction;
        this.imageUrl = imageUrl;
    }
}