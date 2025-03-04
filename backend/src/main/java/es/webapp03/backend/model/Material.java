package es.webapp03.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Course course;

    private String name;

    private String type;

    private String url;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;

    public Material() {
        // Constructor vacío requerido por JPA
    }

    public Material(String name, String type, byte[] file, Course course) {
        this.name = name;
        this.type = type;
        this.course = course;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Course getcourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
