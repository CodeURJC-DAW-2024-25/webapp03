package es.webapp03.backend.services;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.mustache.MustacheViewResolver;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class HtmlToPdfConverterService {

    @Autowired
    private MustacheViewResolver mustacheViewResolver;

    public void generatePdfFromTemplate(String templatePath, Map<String, Object> data, String pdfFilePath) throws IOException {
        // Read HTML template content from file
        String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

        // Render the template with Mustache
        String htmlContent = mustacheViewResolver.getTemplate(templatePath).execute(data);

        // Parse HTML content to XHTML
        Document document = Jsoup.parse(htmlContent);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        // Generate PDF from XHTML
        try (OutputStream outputStream = new FileOutputStream(pdfFilePath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(new W3CDom().fromJsoup(document), null);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}