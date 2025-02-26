package es.webapp03.backend.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

@Service
public class HtmlToPdfConverterService {

    @Autowired
    private MustacheViewResolver mustacheViewResolver;

    @Autowired
    public Mustache.Compiler mustacheCompiler;

    public void generatePdfFromTemplate(String templatePath, Map<String, Object> data, String pdfFilePath) throws IOException {
        // Read HTML template content from file
        String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

        // Render the template with Mustache
        Template template = mustacheCompiler.compile(templateContent);
        String htmlContent = template.execute(data);

        // Parse HTML content to XHTML
        org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(htmlContent);
        jsoupDocument.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        // Convert Jsoup Document to W3C Document
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Generate PDF from XHTML
        try (OutputStream outputStream = new FileOutputStream(pdfFilePath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(w3cDocument, null);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}