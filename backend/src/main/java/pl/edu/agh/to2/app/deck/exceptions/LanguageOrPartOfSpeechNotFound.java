package pl.edu.agh.to2.app.deck.exceptions;

public class LanguageOrPartOfSpeechNotFound extends RuntimeException {
    public LanguageOrPartOfSpeechNotFound() {
        super("Language or part of speech not found");
    }
}
