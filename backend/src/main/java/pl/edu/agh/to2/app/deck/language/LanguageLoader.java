package pl.edu.agh.to2.app.deck.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LanguageLoader {
    private LanguageConfig config = null;

    public LanguageLoader() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("config/languages-config.json");
        this.config = objectMapper.readValue(resource.getInputStream(), LanguageConfig.class);
    }

    public LanguageConfig getConfig() {
        return config;
    }
}
