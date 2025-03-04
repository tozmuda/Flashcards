package pl.edu.agh.to2.app.deck.exceptions;

public class FlashcardOriginalAlreadyExistsException extends RuntimeException {

    public FlashcardOriginalAlreadyExistsException() {
        super("Flashcard definition already exists in deck");
    }
}
