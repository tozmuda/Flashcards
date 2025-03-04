package pl.edu.agh.to2.app.deck.flashcard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.edu.agh.to2.app.deck.exceptions.FlashcardEmptyException;

import java.util.List;

public record Flashcard(String original, String translation, String baseForm, String partOfSpeech,
                        String transcription, String transliteration) {
    public Flashcard(String original, String translation, String baseForm, String partOfSpeech, String transcription, String transliteration) {
        this.original = original.toLowerCase().trim();
        this.translation = translation.toLowerCase().trim();
        this.baseForm = baseForm.toLowerCase().trim();
        this.transcription = transcription.toLowerCase().trim();
        this.transliteration = transliteration.toLowerCase().trim();
        this.partOfSpeech = partOfSpeech;
    }

    public void validate() {
        if (original == null || translation == null || baseForm == null || partOfSpeech == null ||
                original.isBlank() || translation.isBlank() || baseForm.isBlank()) {
            throw new FlashcardEmptyException();
        }
    }

    @JsonIgnore
    public List<String> getFields() {
        return List.of(original, partOfSpeech, baseForm, translation, transcription, transliteration);
    }
}
