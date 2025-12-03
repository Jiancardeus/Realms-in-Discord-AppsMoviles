package com.example.demo.repository;

import com.example.demo.dto.Deck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeckRepository extends MongoRepository<Deck, String> {
    // Buscar todos los mazos de un usuario específico
    List<Deck> findByUsername(String username);
}