package es.webapp03.backend.dto;

public class PdfRequestDTO {
    private String templateName;
    private String outputFileName;

    // Getters and Setters
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}