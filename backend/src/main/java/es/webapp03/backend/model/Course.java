package es.webapp03.backend.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    @Lob
    private Blob imageFile;

    private boolean image;

    @ManyToMany(mappedBy = "courses")
    private List<User> users;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials = new ArrayList<>();

    @ElementCollection
    private List<String> tags;

    public Course() {
        // Constructor vacío requerido por JPA
    }

    public Course(String title, String description, Blob imageFile) {
        this.title = title;
        this.description = description;
        this.imageFile = imageFile;
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

    public boolean getImage() {
        return this.image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob image) {
        this.imageFile = image;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
