package com.example.demo.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "decks")
public class Deck {
    @Id
    private String id; // ID único del mazo en MongoDB

    private String name;
    private String username; // Dueño del mazo
    private String faction;
    private List<DeckCardItem> cards; // Lista de cartas y sus cantidades

    // Clase interna para guardar ID de carta y cantidad
    @Data
    public static class DeckCardItem {
        private String cardId; // El ID de mongo de la carta (ej: "692fe...")
        private int count;     // Cuántas copias (1, 2 o 3)

        public DeckCardItem() {}
        public DeckCardItem(String cardId, int count) {
            this.cardId = cardId;
            this.count = count;
        }
    }
}