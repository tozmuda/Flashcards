package pl.edu.agh.to2.app.text;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.app.dto.TextDTO;
import pl.edu.agh.to2.app.exception.ApiRequestException;

import java.util.List;

@RestController
@RequestMapping("/text")
class TextController {
    TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    /**
     * Endpoint for parsing a sentence sent by the user
     * Request body should look like {"text": "Hello world!"}
     * @param textDTO an object containing the sentence to parse
     * @return ResponseEntity with status 200 and List of Strings if there is at least one word in the sentence, 400 otherwise
     */
    @PostMapping
    public ResponseEntity<List<String>> parseText(@RequestBody TextDTO textDTO) {
        List<String> results = textService.parseText(textDTO.text());
        if (results.isEmpty()) {
            throw new ApiRequestException("A sentence must contain at least one word", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(results);
    }
}
