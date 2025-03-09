package es.webapp03.backend.service;

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

    public byte[] generatePdfFromTemplate(String templateContent, Map<String, Object> data) throws IOException {
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