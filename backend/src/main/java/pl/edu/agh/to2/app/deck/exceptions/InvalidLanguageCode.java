package pl.edu.agh.to2.app.deck.exceptions;

public class InvalidLanguageCode extends RuntimeException {
    public InvalidLanguageCode() {super("Language code not found");}
}
