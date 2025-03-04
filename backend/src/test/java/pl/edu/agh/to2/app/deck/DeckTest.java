package pl.edu.agh.to2.app.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardEmptyException;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardOriginalAlreadyExistsException;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.deck.language.LanguageLoader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        try {
            deck = new Deck(new LanguageLoader());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    void addFlashcardTest() {
        Flashcard flashcard1 = new Flashcard("dog", "pies", "dog", "noun", "dog", "dog");
        Flashcard flashcard2 = new Flashcard("Dog", "kot", "dog", "noun", "", "");
        Flashcard flashcard3 = new Flashcard("cat", "kot", "cat", "noun", "", "");
        Flashcard flashcard4 = new Flashcard("", "", "", "noun", "", "");

        assertDoesNotThrow(() -> deck.addFlashcard(flashcard1));
        assertDoesNotThrow(() -> deck.addFlashcard(flashcard3));
        assertThrows(FlashcardOriginalAlreadyExistsException.class, () -> deck.addFlashcard(flashcard2));
        assertThrows(FlashcardEmptyException.class, () -> deck.addFlashcard(flashcard4));
    }

    @Test
    void resetTest() {
        Flashcard flashcard = new Flashcard("dog", "pies", "dog", "noun", "dog", "dog");

        assertDoesNotThrow(() -> deck.addFlashcard(flashcard));
        deck.reset();
        assertEquals(0, deck.getAllFlashcards().size());
        assertDoesNotThrow(() -> deck.addFlashcard(flashcard));
    }
}
