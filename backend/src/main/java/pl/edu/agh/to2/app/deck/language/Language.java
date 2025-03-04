package pl.edu.agh.to2.app.deck.language;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Language {
    private String name;
    @JsonProperty("short")
    private String shortCode;
    private String conjunction;
    private List<Part> parts;

    public String getName() {
        return name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getConjunction() {
        return conjunction;
    }

    public List<Part> getParts() {
        return parts;
    }

    public static class Part {
        private String partOfSpeech;
        private List<String> possiblePartsOfSentence;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<String> getPossiblePartsOfSentence() {
            return possiblePartsOfSentence;
        }
    }
}
