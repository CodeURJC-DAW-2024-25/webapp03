package es.webapp03.backend.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long course_id;

    private String name;

    private String type;

    private String url;

    @Lob
	private Blob file;

    public Material(String name, String type, String url, Blob file) {
		this.name = name;
		this.type = type;
		this.url = url;
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

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public Long getcourseId() {
        return course_id;
    }

    public void setCourseId(Long course_id) {
        this.course_id = course_id;
    }

}
