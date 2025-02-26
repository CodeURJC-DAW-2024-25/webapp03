package es.webapp03.backend.controller;

import com.example.pdfgenerator.service.HtmlToPdfConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final HtmlToPdfConverterService htmlToPdfConverterService;

    @Autowired
    public PdfController(HtmlToPdfConverterService htmlToPdfConverterService) {
        this.htmlToPdfConverterService = htmlToPdfConverterService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestParam String templateName, @RequestParam String outputFileName) {
        try {
            String pdfFilePath = "output/" + outputFileName + ".pdf"; // Adjust the output path as needed
            htmlToPdfConverterService.convertHtmlToPdf(templateName, pdfFilePath);
            return ResponseEntity.ok("PDF generated successfully: " + pdfFilePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }
}