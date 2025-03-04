package pl.edu.agh.to2.app.text;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TextService {
    private final Parser parser;

    public TextService(Parser parser) {
        this.parser = parser;
    }

    public List<String> parseText(String text) {
        return parser.parse(text);
    }
}
