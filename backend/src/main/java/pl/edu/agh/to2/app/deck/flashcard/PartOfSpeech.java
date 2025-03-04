package pl.edu.agh.to2.app.deck.flashcard;

public enum PartOfSpeech {
    NOT_SELECTED("Not selected"),
    NOUN("Noun"),
    VERB("Verb"),
    ADJECTIVE("Adjective"),
    ADVERB("Adverb"),
    PRONOUN("Pronoun"),
    NUMERAL("Numeral"),
    PREPOSITION("Preposition"),
    CONJUNCTION("Conjunction"),
    PARTICLE("Particle"),
    INTERJECTION("Interjection"),
    ARTICLE("Article"),
    OTHER("Other");

    private final String displayName;

    PartOfSpeech(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
