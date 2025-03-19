package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.dto.PdfRequestDTO;
import es.webapp03.backend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/pdf")
public class PdfRestController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate/{courseId}")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequestDTO pdfRequest, Principal principal, @PathVariable Long courseId) {
        try {
            return pdfService.createPdf(pdfRequest, principal, courseId);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}