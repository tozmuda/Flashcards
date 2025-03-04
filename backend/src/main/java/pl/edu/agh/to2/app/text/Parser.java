package pl.edu.agh.to2.app.text;



import org.springframework.stereotype.Service;
import pl.edu.agh.to2.app.deck.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
class Parser {

    private final Deck deck;

    public Parser(Deck deck) {
        this.deck = deck;
    }

    List<String> parseWithPunctuation(String text) {
        String regex = "\\p{L}+|[\\p{Punct}]";

        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            result.add(matcher.group());
        }

        if (!result.isEmpty() && !result.get(result.size() - 1).equals(".")) {
            result.add(".");
        }

        return result;
    }

    public List<String> parse(String text) {
        if (text == null || Objects.equals(text, "")) return List.of();

        String cleanedText = text.replaceAll("[^\\p{L}\\p{M}\\s]", " ").trim().toLowerCase();

        if (cleanedText.isEmpty()) return List.of();

        String[] words = cleanedText.split("\\s+");
        
        deck.setOriginalText(parseWithPunctuation(text));

        return Arrays.stream(words)
                .distinct()
                .toList();
    }
}
