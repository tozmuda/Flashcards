package pl.edu.agh.to2.app.functionalTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.to2.app.deck.Deck;
import pl.edu.agh.to2.app.deck.database.FlashcardDTO;
import pl.edu.agh.to2.app.deck.database.FlashcardRepository;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeckPersistenceFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Deck deck;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Test
    void saveDeck_PersistsFlashcards() throws Exception {
        // Arrange
        Flashcard flashcard1 = new Flashcard("psa", "dog", "pies", "noun", "", "");
        Flashcard flashcard2 = new Flashcard("kota", "cat", "kot", "noun", "", "");
        deck.addFlashcard(flashcard1);
        deck.addFlashcard(flashcard2);

        // Act
        MvcResult result = mockMvc.perform(get("/deck/save"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String expectedJson = "{\"message\":\"Added 2 flashcards out of 2 flashcards. Ignored 0 flashcards due to duplicates.\"}";
        assertEquals(expectedJson, result.getResponse().getContentAsString());

        List<FlashcardDTO> flashcards = flashcardRepository.findAll();
        assertEquals(2, flashcards.size());
    }
}