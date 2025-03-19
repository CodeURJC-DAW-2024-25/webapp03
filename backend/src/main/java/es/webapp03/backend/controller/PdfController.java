package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.dto.PdfRequestDTO;
import es.webapp03.backend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping(value = "/pdf/generate/{courseId}", consumes = "application/json")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequestDTO pdfRequest, @RequestParam("_csrf") String csrfToken, Principal principal, @PathVariable Long courseId) {
        try {
            return pdfService.createPdf(pdfRequest, principal, courseId);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}