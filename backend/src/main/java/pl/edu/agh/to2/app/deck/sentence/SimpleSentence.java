package pl.edu.agh.to2.app.deck.sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleSentence {
    private final List<TextElement> textElements;
    private int level = 0;

    public SimpleSentence() {
        textElements = new ArrayList<>();
    }

    public SimpleSentence(SimpleSentence simpleSentence) {
        textElements = new ArrayList<>();
        textElements.addAll(simpleSentence.textElements);
        level = simpleSentence.level;
    }

    public void addTextElement(TextElement textElement) {
        textElements.add(textElement);
    }

    public List<TextElement> getTextElements() {
        return Collections.unmodifiableList(textElements);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SimpleSentence{" +
                "textElements=" + textElements +
                ", level=" + level +
                '}';
    }
}
