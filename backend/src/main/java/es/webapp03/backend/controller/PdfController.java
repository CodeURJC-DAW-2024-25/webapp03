package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/api/pdf/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String templateName, @RequestParam String outputFileName, Model model) throws IOException, DocumentException {
        // Load the template content from file
        String templatePath = "src/main/resources/templates/" + templateName + ".html";
        String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

        // Prepare data for the template
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("model", model);

        // Generate PDF
        byte[] pdfBytes = pdfService.generatePdfFromTemplate(templateContent, modelMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", outputFileName + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}