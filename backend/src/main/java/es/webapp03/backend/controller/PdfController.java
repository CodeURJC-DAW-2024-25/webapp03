package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/pdf/generate/{courseId}")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String templateName, @RequestParam String outputFileName, @RequestParam("_csrf") String csrfToken, Principal principal, @PathVariable Long courseId) {
        try {
            return pdfService.createPdf(templateName, outputFileName, principal, courseId);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}