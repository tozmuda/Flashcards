package pl.edu.agh.to2.app.deck.sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sentence {
    private final List<SimpleSentence> simpleSentences;

    public Sentence() {
        simpleSentences = new ArrayList<>();
    }

    public Sentence(Sentence sentence) {
        simpleSentences = new ArrayList<>();
        simpleSentences.addAll(sentence.simpleSentences);
    }

    public void addSimpleSentence(SimpleSentence sentence) {
        simpleSentences.add(sentence);
    }

    public List<SimpleSentence> getSimpleSentences() {
        return Collections.unmodifiableList(simpleSentences);
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "simpleSentences=" + simpleSentences +
                '}';
    }
}
