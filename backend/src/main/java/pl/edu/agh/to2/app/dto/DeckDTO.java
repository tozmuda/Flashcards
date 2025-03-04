package pl.edu.agh.to2.app.dto;

import pl.edu.agh.to2.app.deck.flashcard.Flashcard;

import java.util.List;

public record DeckDTO(List<Flashcard> flashcards) {

    public DeckDTO(List<Flashcard> flashcards) {
        this.flashcards = List.copyOf(flashcards);
    }
}

