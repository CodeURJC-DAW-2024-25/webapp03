package es.webapp03.backend.service;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.dto.PdfRequestDTO;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

@Service
public class PdfService {

    @Autowired
    private Mustache.Compiler mustacheCompiler;

    public ResponseEntity<byte[]> createPdf(PdfRequestDTO pdfRequest, Principal principal, Long courseId) throws IOException, DocumentException {
        if (principal == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        // Load the template content from classpath
        ClassPathResource resource = new ClassPathResource("templates/" + pdfRequest.getTemplateName() + ".html");
        String templateContent = new String(Files.readAllBytes(resource.getFile().toPath()));

        // Get the current user
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.status(404).body(null); // User not found
        }

        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Course not found
        }
        Course course = courseOpt.get();

        // Convert image to base64
        String base64Image = null;
        if (course.getImageFile() != null) {
            Blob imageBlob = course.getImageFile();
            try (InputStream inputStream = imageBlob.getBinaryStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                base64Image = Base64.getEncoder().encodeToString(imageBytes);
            } catch (SQLException e) {
                return ResponseEntity.status(500).body(null); // Error reading image
            }
        }

        // Prepare data for the template
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("userFormalName", user.getName()); // Set userName as a string
        modelMap.put("course", course);
        modelMap.put("courseImageUrl", base64Image != null ? "data:image/jpeg;base64," + base64Image : null);

        // Generate PDF
        byte[] pdfBytes = renderPdfFromHtmlTemplate(templateContent, modelMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", pdfRequest.getOutputFileName() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    public byte[] renderPdfFromHtmlTemplate(String templateContent, Map<String, Object> data) throws IOException {
        // Render the template with Mustache
        Template template = mustacheCompiler.compile(templateContent);
        String htmlContent = template.execute(data);

        // Parse HTML content to XHTML
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(htmlContent);
        jsoupDocument.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        // Convert Jsoup Document to W3C Document
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Generate PDF from XHTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(w3cDocument, null);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}