package pl.edu.agh.to2.app.deck;

public class AddedIgnoredStatus {

    private int addedFlashcards;
    private int ignoredFlashcards;

    public AddedIgnoredStatus() {
        addedFlashcards = 0;
        ignoredFlashcards = 0;
    }

    public void incrementAddedFlashcards() {
        addedFlashcards++;
    }

    public void incrementIgnoredFlashcards() {
        ignoredFlashcards++;
    }

    public int getAddedFlashcards() {
        return addedFlashcards;
    }

    public int getIgnoredFlashcards() {
        return ignoredFlashcards;
    }

    public int getSum() {
        return addedFlashcards + ignoredFlashcards;
    }

}
