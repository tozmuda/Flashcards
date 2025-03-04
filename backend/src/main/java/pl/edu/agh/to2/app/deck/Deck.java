package pl.edu.agh.to2.app.deck;

import org.springframework.stereotype.Component;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardOriginalAlreadyExistsException;
import pl.edu.agh.to2.app.deck.exceptions.InvalidLanguageCode;
import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.deck.language.LanguageConfig;
import pl.edu.agh.to2.app.deck.language.LanguageLoader;
import pl.edu.agh.to2.app.deck.language.Language;
import pl.edu.agh.to2.app.deck.sentence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Deck {
    private final Map<String, Flashcard> flashcards;
    private List<String> originalText;
    private List<Sentence> originalSentences;
    private Language language = null;
    private LanguageLoader languageLoader;

    public Deck(LanguageLoader languageLoader) {
        this.flashcards = new LinkedHashMap<>();
        this.originalSentences = new ArrayList<>();
        this.originalText = new ArrayList<>();
        this.languageLoader = languageLoader;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcard.validate();
        if (flashcards.containsKey(flashcard.original()))
            throw new FlashcardOriginalAlreadyExistsException();
        flashcards.put(flashcard.original(), flashcard);
    }

    public List<Flashcard> getAllFlashcards() {
        return new ArrayList<>(flashcards.values());
    }

    public void reset() {
        flashcards.clear();
        originalSentences.clear();
        originalText.clear();
    }

    public List<Sentence> getOriginalSentences() {
        return originalSentences;
    }

    public void setOriginalSentences(List<Sentence> originalSentences) {
        this.originalSentences = originalSentences;
    }

    public List<String> getOriginalText() {
        return Collections.unmodifiableList(originalText);
    }

    public void setOriginalText(List<String> originalText) {
        this.originalText = originalText;
    }

    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }

    public Language getLanguage() {
        return language;
    }

    public Map<String, Flashcard> getFlashcards() {
        return Collections.unmodifiableMap(flashcards);
    }

    public void setLanguage(String languageShort) throws InvalidLanguageCode {
        LanguageConfig config = languageLoader.getConfig();
        config.getLanguages().forEach(lang -> {
            if (lang.getShortCode().equals(languageShort)) {
                this.language = lang;
            }
        });
        if(this.language == null) throw new InvalidLanguageCode();
    }

    public String getLanguageShort(){
        return this.language.getShortCode();
    }


}
