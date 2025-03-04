package pl.edu.agh.to2.app.filegenerator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.app.exception.ApiRequestException;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/pdf")
public class PDFController {
    SentencesPDFCreatorService sentencesPDFCreatorService;
    private final Logger logger = Logger.getLogger(PDFController.class.getName());
    public PDFController(SentencesPDFCreatorService sentencesPDFCreatorService) {
        this.sentencesPDFCreatorService = sentencesPDFCreatorService;
    }

    @GetMapping
    public ResponseEntity<byte[]> makePDF() {
        try {
            byte[] pdfBytes = sentencesPDFCreatorService.createPDF();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=sentences.pdf")
                    .body(pdfBytes);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new ApiRequestException("Error while creating PDF", HttpStatus.BAD_REQUEST);
        }
    }



}
