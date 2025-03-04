package pl.edu.agh.to2.app.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.deck.language.LanguageLoader;
import pl.edu.agh.to2.app.deck.sentence.Punctuation;
import pl.edu.agh.to2.app.deck.sentence.Sentence;
import pl.edu.agh.to2.app.deck.sentence.SimpleSentence;
import pl.edu.agh.to2.app.deck.sentence.TextElement;
import pl.edu.agh.to2.app.deck.sentence.Word;
import pl.edu.agh.to2.app.dto.PartsOfSentenceDTO;
import pl.edu.agh.to2.app.dto.SentenceDTOGetSentences;
import pl.edu.agh.to2.app.dto.SimpleSentenceDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckSentencesServiceTest {
    // ----------------------- !! -----------------------
    // Zdecydowałem, że nie będę mockował Deck, ponieważ wtedy trzebaby zamockować metodę
    // getOriginalSentences i zwrócić całe drzewo zdań, więc pozostawię tą klasę jako
    // realizującą testy integracyjne.
    // W DeckTest powinien być test jednostkowy do metody m.in. addFlashcard(), która jest tutaj używana
    private DeckSentencesService deckSentencesService;

    @BeforeEach
    void setup() throws IOException {
        this.deckSentencesService = new DeckSentencesService(new Deck(new LanguageLoader()));
    }

    @Test
    void test_buildSentencesTreeAndGetSentences() {

        // Arrange
        List<String> originalText = List.of("Ala", "ma", "kota", ",", "a", "Basia", "ma", "psa", ".",
                "Pada", "deszcz", ".");
        Flashcard flashcard1 = new Flashcard("Ala",
                "ala", "ala", "noun", "", "");
        Flashcard flashcard2 = new Flashcard("ma",
                "has", "miec", "verb", "", "");
        Flashcard flashcard3 = new Flashcard("kota",
                "cat", "kot", "noun", "", "");
        Flashcard flashcard4 = new Flashcard("a",
                "and", "a", "conjunction", "", "");
        Flashcard flashcard5 = new Flashcard("Basia",
                "basia", "basia", "noun", "", "");
        Flashcard flashcard6 = new Flashcard("psa",
                "dog", "pies", "noun", "", "");
        Flashcard flashcard7 = new Flashcard("Pada",
                "rain", "padac", "verb", "", "");
        Flashcard flashcard8 = new Flashcard("deszcz",
                "rain", "deszcz", "noun", "", "");
        List<Flashcard> flashcardList = List.of(flashcard1, flashcard2, flashcard3, flashcard4, flashcard5, flashcard6, flashcard7, flashcard8);
        for (Flashcard flashcard : flashcardList) {
            deckSentencesService.getDeck().addFlashcard(flashcard);
        }
        deckSentencesService.getDeck().setOriginalText(originalText);
        deckSentencesService.getDeck().setLanguage("PL");

        // Act
        List<SentenceDTOGetSentences> result = deckSentencesService.buildSentencesTreeAndGetSentences();

        // Assert
        assertEquals(new Punctuation(",").getSymbol(), ((Punctuation) deckSentencesService.getDeck().getOriginalSentences().getFirst()
                .getSimpleSentences()
                .getFirst()
                .getTextElements()
                .get(3)).getSymbol());

        assertEquals((new Word(flashcard1)).getFlashcard(), flashcard1);

        assertEquals((new Word(flashcard8)).getFlashcard(), ((Word) deckSentencesService.getDeck().getOriginalSentences().get(1).getSimpleSentences().getFirst().getTextElements().get(1)).getFlashcard());

        assertEquals("basia", result.getFirst().simpleSentences().get(1).words().get(1).originalText());
    }

    @Test
    void test_getPossiblePartsOfSentence() {
        // Arrange
        deckSentencesService.getDeck().setLanguage("PL");

        // Act
        List<String> possiblePartsOfSentence = deckSentencesService.getPossiblePartsOfSentence("verb");

        // Assert
        assertEquals(List.of("predicate", "linking verb"), possiblePartsOfSentence);
    }

    @Test
    void test_fillPartsOfSentences() {
        // Arrange
        List<String> originalText = List.of("Ala", "ma", "kota", ",", "a", "Basia", "ma", "psa", ".",
                "Pada", "deszcz", ".");
        Flashcard flashcard1 = new Flashcard("Ala",
                "ala", "ala", "noun", "", "");
        Flashcard flashcard2 = new Flashcard("ma",
                "has", "miec", "verb", "", "");
        Flashcard flashcard3 = new Flashcard("kota",
                "cat", "kot", "noun", "", "");
        Flashcard flashcard4 = new Flashcard("a",
                "and", "a", "conjunction", "", "");
        Flashcard flashcard5 = new Flashcard("Basia",
                "basia", "basia", "noun", "", "");
        Flashcard flashcard6 = new Flashcard("psa",
                "dog", "pies", "noun", "", "");
        Flashcard flashcard7 = new Flashcard("Pada",
                "rain", "padac", "verb", "", "");
        Flashcard flashcard8 = new Flashcard("deszcz",
                "rain", "deszcz", "noun", "", "");
        List<Flashcard> flashcardList = List.of(flashcard1, flashcard2, flashcard3, flashcard4, flashcard5, flashcard6, flashcard7, flashcard8);
        for (Flashcard flashcard : flashcardList) {
            deckSentencesService.getDeck().addFlashcard(flashcard);
        }
        deckSentencesService.getDeck().setOriginalText(originalText);
        deckSentencesService.getDeck().setLanguage("PL");

        PartsOfSentenceDTO partsOfSentenceDTO1 = new PartsOfSentenceDTO(0, new ArrayList<>());
        partsOfSentenceDTO1.selectedPartsOfSentence().addAll(List.of("predicate noun", "linking verb", "predicate noun", ""));
        PartsOfSentenceDTO partsOfSentenceDTO2 = new PartsOfSentenceDTO(0, new ArrayList<>());
        partsOfSentenceDTO2.selectedPartsOfSentence().addAll(List.of("connector", "predicate noun", "linking verb", "predicate noun", ""));
        PartsOfSentenceDTO partsOfSentenceDTO3 = new PartsOfSentenceDTO(0, new ArrayList<>());
        partsOfSentenceDTO3.selectedPartsOfSentence().addAll(List.of("linking verb", "predicate noun", ""));

        List<PartsOfSentenceDTO> list1 = List.of(partsOfSentenceDTO1, partsOfSentenceDTO2);
        List<PartsOfSentenceDTO> list2 = List.of(partsOfSentenceDTO3);

        // Act
        deckSentencesService.buildSentencesTreeAndGetSentences();
        deckSentencesService.fillPartsOfSentences(List.of(list1, list2));

        // Assert
        assertEquals("connector", ((Word) deckSentencesService.getDeck().getOriginalSentences().getFirst().getSimpleSentences().get(1).getTextElements().getFirst()).getPartOfSentence());

    }

    @Test
    void test_getFlashcardsFromOriginalText() {
        // Arrange
        List<String> originalText = List.of("Ala", "ma", "kota", ",", "a", "Basia", "ma", "psa", ".",
                "Pada", "deszcz", ".");
        Flashcard flashcard1 = new Flashcard("Ala",
                "ala", "ala", "noun", "", "");
        Flashcard flashcard2 = new Flashcard("ma",
                "has", "miec", "verb", "", "");
        Flashcard flashcard3 = new Flashcard("kota",
                "cat", "kot", "noun", "", "");
        Flashcard flashcard4 = new Flashcard("a",
                "and", "a", "conjunction", "", "");
        Flashcard flashcard5 = new Flashcard("Basia",
                "basia", "basia", "noun", "", "");
        Flashcard flashcard6 = new Flashcard("psa",
                "dog", "pies", "noun", "", "");
        Flashcard flashcard7 = new Flashcard("Pada",
                "rain", "padac", "verb", "", "");
        Flashcard flashcard8 = new Flashcard("deszcz",
                "rain", "deszcz", "noun", "", "");
        List<Flashcard> flashcardList = List.of(flashcard1, flashcard2, flashcard3, flashcard4, flashcard5, flashcard6, flashcard7, flashcard8);
        for (Flashcard flashcard : flashcardList) {
            deckSentencesService.getDeck().addFlashcard(flashcard);
        }
        deckSentencesService.getDeck().setOriginalText(originalText);
        deckSentencesService.getDeck().setLanguage("PL");

        Sentence sentence1 = new Sentence();
        Sentence sentence2 = new Sentence();

        SimpleSentence simpleSentence1 = new SimpleSentence();
        SimpleSentence simpleSentence2 = new SimpleSentence();
        SimpleSentence simpleSentence3 = new SimpleSentence();

        List<TextElement> textElements1 = List.of(new Word(flashcard1, "predicate noun"),
                new Word(flashcard2, "linking verb"),
                new Word(flashcard3, "predicate noun"),
                new Punctuation(","));
        List<TextElement> textElements2 = List.of(new Word(flashcard4, "connector"),
                new Word(flashcard5, "predicate noun"),
                new Word(flashcard2, "linking verb"),
                new Word(flashcard6, "predicate noun"),
                new Punctuation("."));
        List<TextElement> textElements3 = List.of(new Word(flashcard7, "linking verb"),
                new Word(flashcard8, "predicate noun"),
                new Punctuation("."));

        for (TextElement textElement : textElements1) {
            simpleSentence1.addTextElement(textElement);
        }
        for (TextElement textElement : textElements2) {
            simpleSentence2.addTextElement(textElement);
        }
        for (TextElement textElement : textElements3) {
            simpleSentence3.addTextElement(textElement);
        }

        sentence1.addSimpleSentence(simpleSentence1);
        sentence1.addSimpleSentence(simpleSentence2);
        sentence2.addSimpleSentence(simpleSentence3);
        List<Sentence> originalSentences = new ArrayList<>(List.of(sentence1, sentence2));
        deckSentencesService.getDeck().setOriginalSentences(originalSentences);

        // Act
        List<List<SimpleSentenceDTO>> result = deckSentencesService.getAllWords();

        // Assert
        assertEquals("kot", result.getFirst().getFirst().words().get(2).baseForm());
        assertEquals("linking verb", result.getFirst().get(1).words().get(2).partOfSentence());
    }
}
