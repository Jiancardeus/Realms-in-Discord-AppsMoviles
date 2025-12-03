package com.example.demo.repository;

import com.example.demo.dto.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
    // Puedes agregar métodos personalizados si necesitas, por ejemplo:
    List<Card> findByFaction(String faction);
}