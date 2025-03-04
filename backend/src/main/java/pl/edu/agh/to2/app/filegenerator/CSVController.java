package pl.edu.agh.to2.app.filegenerator;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.app.exception.ApiRequestException;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/csv")
class CSVController {
    DeckCSVCreatorService deckCSVCreatorService;
    Logger logger = Logger.getLogger(CSVController.class.getName());
    public CSVController(DeckCSVCreatorService deckCSVCreatorService) {
        this.deckCSVCreatorService = deckCSVCreatorService;
    }
    /**
     * Endpoint for returning a CSV file with flashcards
     * @return ResponseEntity with CSV file in header with HttpStatus OK or BAD_REQUEST if error occurred
     */
    @GetMapping
    public ResponseEntity<byte[]> makeCSV() {
        try {
            byte[] csvData = deckCSVCreatorService.createCSV();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=flashcards.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new ApiRequestException("Error while creating CSV", HttpStatus.BAD_REQUEST);
        }

    }
}
