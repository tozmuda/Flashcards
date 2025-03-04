package pl.edu.agh.to2.app.deck.exceptions;

public class FlashcardEmptyException extends RuntimeException {

    public FlashcardEmptyException() {
        super("Client has sent an empty flashcard");
    }

}
