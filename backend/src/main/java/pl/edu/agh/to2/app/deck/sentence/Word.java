package pl.edu.agh.to2.app.deck.sentence;

import pl.edu.agh.to2.app.deck.flashcard.Flashcard;
import pl.edu.agh.to2.app.dto.TextElementDTOGetSentences;
import java.util.List;

public class Word implements TextElement {
    private final Flashcard flashcard;
    private String partOfSentence;

    public Word(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public Word(Word word) {
        this.flashcard = word.flashcard;
        this.partOfSentence = word.partOfSentence;
    }

    public Word(Flashcard flashcard, String partOfSentence) {
        this.flashcard = flashcard;
        this.partOfSentence = partOfSentence;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public String getPartOfSentence() {
        return partOfSentence;
    }

    public void setPartOfSentence(String partOfSentence) {
        this.partOfSentence = partOfSentence;
    }

    @Override
    public String toString() {
        return "Word{" +
                "flashcard=" + flashcard +
                ", partOfSentence='" + partOfSentence + '\'' +
                '}';
    }

    @Override
    public TextElementDTOGetSentences toDto(List<String> possiblePartsOfSentence) {
        return new TextElementDTOGetSentences(
            flashcard.original(),
            possiblePartsOfSentence
        );
    }

    @Override
    public String getPartOfSpeech() {
        return flashcard.partOfSpeech();
    }
}
