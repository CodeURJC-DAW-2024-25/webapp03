package es.webapp03.backend.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private boolean diploma;

    @Lob
	private Blob imageFile;

    private boolean image;

    public Course(String title, String description, boolean diploma) {
        this.title = title;
        this.description = description;
        this.diploma = diploma;
    }


	public Long getId() {
		return id;
	}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDiploma() {
        return diploma;
    }

    public void setDiploma(boolean diploma) {
        this.diploma = diploma;
    }

    public Blob getImageFile() {
		return imageFile;
	}

	public void setImageFile(Blob image) {
		this.imageFile = image;
	}

    public boolean getImage(){
		return this.image;
	}

	public void setImage(boolean image){
		this.image = image;
	}
}
