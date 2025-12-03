package com.example.demo.controller;

import com.example.demo.dto.Deck;
import com.example.demo.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeckController {

    private final DeckRepository deckRepository;

    // Guardar o Actualizar un mazo
    @PostMapping
    public ResponseEntity<Deck> saveDeck(@RequestBody Deck deck) {
        System.out.println("Guardando mazo para: " + deck.getUsername());
        return ResponseEntity.ok(deckRepository.save(deck));
    }

    // Obtener los mazos de un usuario
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Deck>> getUserDecks(@PathVariable String username) {
        return ResponseEntity.ok(deckRepository.findByUsername(username));
    }

    // Eliminar un mazo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable String id) {
        deckRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}