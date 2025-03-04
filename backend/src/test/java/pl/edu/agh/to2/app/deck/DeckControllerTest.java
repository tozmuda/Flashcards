package pl.edu.agh.to2.app.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardEmptyException;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardOriginalAlreadyExistsException;
import pl.edu.agh.to2.app.deck.exceptions.InvalidLanguageCode;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.dto.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeckController.class)
class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeckService deckService;
    @MockitoBean
    private DeckSentencesService deckSentencesService;

    @BeforeEach
    void setUp() {
        Mockito.reset(deckService);
        Mockito.reset(deckSentencesService);
    }

    @Test
    void testBuildSentencesTreeAndGetSentences() throws Exception {

        // Arrange
        when(deckSentencesService.buildSentencesTreeAndGetSentences()).thenReturn(
                List.of(new SentenceDTOGetSentences(List.of(new SimpleSentenceDTOGetSentences(List.of(
                        new TextElementDTOGetSentences("dog", List.of("object", "subject"))))))));

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/deck/sentences"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(deckSentencesService.buildSentencesTreeAndGetSentences());

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void testFillPartsOfSentences() throws Exception {

        // Arrange
        when(deckSentencesService.fillPartsOfSentences(anyList()))
                .thenReturn(new MessageDTO("Saved levels and parts of sentences"));

        List<List<PartsOfSentenceDTO>> partsOfSentenceDTO = List.of(List.of(mock(PartsOfSentenceDTO.class)));
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(partsOfSentenceDTO);
        String expectedResponseJson = objectMapper.writeValueAsString(new MessageDTO("Saved levels and parts of sentences"));

        // Act & Assert

        MvcResult result = mockMvc.perform(post("/deck/selected_parts_of_sentences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        assertEquals(expectedResponseJson, result.getResponse().getContentAsString());

    }

    @Test
    void testGetAllWords() throws Exception {

        // Arrange
        when(deckSentencesService.getAllWords()).thenReturn(List.of(List.of(
                        new SimpleSentenceDTO(List.of(
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", ""),
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", "")
                        ), 0),
                        new SimpleSentenceDTO(List.of(
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", ""),
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", "")
                        ), 0)
                ),
                List.of(
                        new SimpleSentenceDTO(List.of(
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", ""),
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", "")
                        ), 0),
                        new SimpleSentenceDTO(List.of(
                                new TextElementDTO("kota", "cat", "noun", "subject", "kot", "", ""),
                                new TextElementDTO("psa", "dog", "noun", "object", "pies", "", "")
                        ), 0)
                )
        ));

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(deckSentencesService.getAllWords());

        // Act & Assert

        MvcResult result = mockMvc.perform(get("/deck"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        assertEquals(expectedJson, result.getResponse().getContentAsString());

    }

    @Test
    void testAddSingleFlashcardToDeck_Success() throws Exception {
        // Arrange

        Flashcard flashcard = new Flashcard("psa", "dog", "pies", "noun", "", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(flashcard);

        String expectedResponseJson = postJson;

        doNothing().when(deckService).addFlashcard(flashcard);

        // Act & Assert

        MvcResult result = mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert

        assertEquals(expectedResponseJson, result.getResponse().getContentAsString());
    }

    @Test
    void testAddSingleFlashcardToDeck_ThrowsFlashcardEmptyException() throws Exception {

        // Arrange

        Flashcard flashcard = new Flashcard("psa", "dog", "", "noun", "", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(flashcard);

        doThrow(new FlashcardEmptyException()).when(deckService).addFlashcard(flashcard);

        // Act & Assert

        mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddSingleFlashcardToDeck_ThrowsFlashcardOriginalAlreadyExistsException() throws Exception {

        // Arrange

        Flashcard flashcard = new Flashcard("psa", "dog", "pies", "noun", "", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(flashcard);

        doThrow(new FlashcardOriginalAlreadyExistsException()).when(deckService).addFlashcard(flashcard);

        // Act & Assert

        mockMvc.perform(post("/deck/new_flashcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResetDeck() throws Exception {

        // Arrange

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(new MessageDTO("Deck has been reset"));

        when(deckService.resetDeck()).thenReturn(new MessageDTO("Deck has been reset"));

        // Act & Assert

        MvcResult result = mockMvc.perform(delete("/deck"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void saveDeck_ReturnsCorrectMessage() throws Exception {
        AddedIgnoredStatus status = new AddedIgnoredStatus();
        status.incrementAddedFlashcards();
        status.incrementAddedFlashcards();
        status.incrementIgnoredFlashcards();
        when(deckService.saveDeck()).thenReturn(status);

        MvcResult result = mockMvc.perform(get("/deck/save"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = "{\"message\":\"Added 2 flashcards out of 3 flashcards. Ignored 1 flashcards due to duplicates.\"}";
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void testGetLang() throws Exception {
        // Assert

        when(deckService.getLanguages()).
                thenReturn(List.of(
                        new LanguageNameShortDTO("English", "EN"),
                        new LanguageNameShortDTO("Polish", "PL")));

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(deckService.getLanguages());

        // Act & Assert

        MvcResult result = mockMvc.perform(get("/deck/lang"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        assertEquals(expectedJson, result.getResponse().getContentAsString());

    }

    @Test
    void testGetPartsOfSpeech() throws Exception {
        // Assert

        when(deckService.getPartsOfSpeech()).
                thenReturn(List.of("object", "subject", "preposition"));

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(deckService.getPartsOfSpeech());

        // Act & Assert

        MvcResult result = mockMvc.perform(get("/deck/parts_of_speech"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void testSetLanguage() throws Exception {
        // Arrange
        LanguageShortDTO languageShortDTO = new LanguageShortDTO("PL");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(languageShortDTO);

        // Act & Assert
        mockMvc.perform(post("/deck/set_language")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isOk());
    }

    @Test
    void testSetLanguage_InvalidLanguage() throws Exception {
        // Arrange
        LanguageShortDTO languageShortDTO = new LanguageShortDTO("PL");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(languageShortDTO);

        doThrow(InvalidLanguageCode.class)
                .when(deckService).setLanguage(languageShortDTO.languageShort());

        // Act & Assert
        mockMvc.perform(post("/deck/set_language")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isBadRequest());
    }
}
