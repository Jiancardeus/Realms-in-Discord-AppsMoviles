package com.example.demo.controller;

import com.example.demo.dto.Card;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards") // 👈 ESTO ES LO QUE BUSCA TU APP ANDROID
@RequiredArgsConstructor
@CrossOrigin("*")
public class CardController {

    private final CardRepository cardRepository;

    // Obtener todas las cartas (Android llama aquí)
    @GetMapping
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Crear una carta (Para que tú puedas llenar la BD desde Postman)
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        return ResponseEntity.ok(cardRepository.save(card));
    }

    // Crear muchas cartas a la vez (Para llenar la BD rápido)
    @PostMapping("/batch")
    public ResponseEntity<List<Card>> createCards(@RequestBody List<Card> cards) {
        return ResponseEntity.ok(cardRepository.saveAll(cards));
    }
}