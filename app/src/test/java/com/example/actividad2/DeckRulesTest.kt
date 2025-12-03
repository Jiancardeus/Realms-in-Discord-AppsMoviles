package com.example.realmsindiscord

import com.example.realmsindiscord.domain.models.DeckCard
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeckRulesTest {

    @Test
    fun `isValidCount returns true when count is between 1 and 3`() {
        // Arrange (Preparar)
        val cardOneCopy = DeckCard(cardId = "1", count = 1)
        val cardThreeCopies = DeckCard(cardId = "2", count = 3)

        // Act & Assert (Ejecutar y Verificar)
        assertTrue("1 copia debería ser válido", cardOneCopy.isValidCount())
        assertTrue("3 copias debería ser válido", cardThreeCopies.isValidCount())
    }

    @Test
    fun `isValidCount returns false when count is greater than 3`() {
        // Arrange
        val cardFourCopies = DeckCard(cardId = "1", count = 4)

        // Act & Assert
        assertFalse("4 copias NO debería ser válido", cardFourCopies.isValidCount())
    }
}