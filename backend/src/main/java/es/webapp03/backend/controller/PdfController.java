package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.dto.PdfRequestDTO;
import es.webapp03.backend.service.PdfService;
import es.webapp03.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/pdf/generate/{courseId}")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequestDTO pdfRequest, @RequestParam("_csrf") String csrfToken, Principal principal, @PathVariable Long courseId) {
        try {
            return pdfService.createPdf(pdfRequest, principal, courseId);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}