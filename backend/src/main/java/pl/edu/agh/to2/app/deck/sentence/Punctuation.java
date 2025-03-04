package pl.edu.agh.to2.app.deck.sentence;

import pl.edu.agh.to2.app.dto.TextElementDTOGetSentences;

import java.util.ArrayList;
import java.util.List;

public class Punctuation implements TextElement {
    private final String symbol;

    public Punctuation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Punctuation{" +
                "symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public TextElementDTOGetSentences toDto(List<String> possiblePartsOfSentence) {
        return new TextElementDTOGetSentences(
                symbol,
                new ArrayList<>()
        );
    }

    @Override
    public String getPartOfSpeech() {
        return null;
    }
}
