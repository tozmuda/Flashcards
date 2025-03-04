package pl.edu.agh.to2.app.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import pl.edu.agh.to2.app.deck.database.FlashcardDTO;
import pl.edu.agh.to2.app.deck.database.FlashcardRepository;
import pl.edu.agh.to2.app.deck.exceptions.InvalidLanguageCode;
import pl.edu.agh.to2.app.deck.language.LanguageLoader;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardEmptyException;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardOriginalAlreadyExistsException;
import pl.edu.agh.to2.app.dto.LanguageNameShortDTO;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.io.IOException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    private DeckService deckService;

    @Mock
    private Deck deck;
    @Mock
    private FlashcardRepository flashcardRepository;

    @BeforeEach
    void setup() throws IOException {
        deckService = new DeckService(deck, flashcardRepository, new LanguageLoader());
    }

    @Test
    void testAddFlashcard_Success() throws FlashcardEmptyException, FlashcardOriginalAlreadyExistsException {
        // Arrange
        Flashcard flashcard = new Flashcard("psa", "dog", "pies", "noun", "", "");

        // Act
        deckService.addFlashcard(flashcard);

        // Assert
        verify(deck, times(1)).addFlashcard(flashcard);
    }

    @Test
    void testAddFlashcard_EmptyOriginal() throws FlashcardEmptyException, FlashcardOriginalAlreadyExistsException{
        // Arrange
        Flashcard emptyOriginal = new Flashcard("", "dog", "pies", "noun", "", "");

        doThrow(new FlashcardEmptyException()).when(deck).addFlashcard(emptyOriginal);

        // Act & Assert
        assertThrows(FlashcardEmptyException.class, () -> deckService.addFlashcard(emptyOriginal));

    }

    @Test
    void testAddFlashcard_EmptyTranslation() throws FlashcardEmptyException, FlashcardOriginalAlreadyExistsException{
        // Arrange
        Flashcard emptyTranslation = new Flashcard("psa", "", "pies", "noun", "", "");

        doThrow(new FlashcardEmptyException()).when(deck).addFlashcard(emptyTranslation);

        // Act & Assert
        assertThrows(FlashcardEmptyException.class, () -> deckService.addFlashcard(emptyTranslation));

    }

    @Test
    void testAddFlashcard_DuplicateOriginal() throws FlashcardEmptyException, FlashcardOriginalAlreadyExistsException{
        // Arrange
        Flashcard duplicateFlashcard = new Flashcard("psa", "dog", "pies", "noun", "", "");

        doThrow(new FlashcardOriginalAlreadyExistsException()).when(deck).addFlashcard(duplicateFlashcard);

        // Act & Assert
        assertThrows(FlashcardOriginalAlreadyExistsException.class, () -> deckService.addFlashcard(duplicateFlashcard));
    }

    @Test
    void testSaveDeckAllFlashcardsSaved() {
        // Arrange
        List<Flashcard> flashcardList = List.of(
                new Flashcard("psa", "dog", "pies", "noun", "", ""),
                new Flashcard("kota", "cat", "kot", "noun", "", "")
        );

        // Act
        when(deck.getAllFlashcards()).thenReturn(flashcardList);
        AddedIgnoredStatus status = deckService.saveDeck();

        // Assert
        assertEquals(2, status.getAddedFlashcards());
        assertEquals(0, status.getIgnoredFlashcards());
        verify(flashcardRepository, times(2)).save(any(FlashcardDTO.class));
    }

    @Test
    void testSaveDeckDuplicateFlashcardsIgnored() {
        // Arrange
        List<Flashcard> flashcardList = List.of(
                new Flashcard("psa", "dog", "pies", "noun", "", ""),
                new Flashcard("psa", "dog", "pies", "noun", "", "")
        );

        when(deck.getAllFlashcards()).thenReturn(flashcardList);
        doThrow(new DataIntegrityViolationException("Duplicate")).when(flashcardRepository).save(any(FlashcardDTO.class));

        // Act
        AddedIgnoredStatus status = deckService.saveDeck();

        // Assert
        assertEquals(0, status.getAddedFlashcards());
        assertEquals(2, status.getIgnoredFlashcards());
        verify(flashcardRepository, times(2)).save(any(FlashcardDTO.class));
    }

    @Test
    void testGetLanguages() {

        // Act
        List<LanguageNameShortDTO> allLanguages = List.of(new LanguageNameShortDTO("Polish", "PL"),
                new LanguageNameShortDTO("English", "EN"),
                new LanguageNameShortDTO("Spanish", "ES"));

        // Assert
        assertEquals(allLanguages, deckService.getLanguages());

    }

    @Test
    void testGetPartsOfSpeech() {

        // Arrange
        List<String> allPartsOfSpeech = List.of("noun", "verb", "adjective", "adverb", "pronoun",
                "numeral", "article", "conjunction", "preposition", "interjection", "not selected", "other");
        when(deck.getLanguageShort()).thenReturn("PL");
        // Act

        List<String> result = deckService.getPartsOfSpeech();

        // Assert
        assertEquals(allPartsOfSpeech, result);
    }

    @Test
    void testSetLanguage_InvalidLanguage() {
        // Arrange
        String languageShort = "ABCDEF";
        doThrow(InvalidLanguageCode.class).when(deck).setLanguage(languageShort);

        // Act & Assert
        assertThrows(InvalidLanguageCode.class, () -> deckService.setLanguage(languageShort));
    }
}
