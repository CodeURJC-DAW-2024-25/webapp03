package es.webapp03.backend.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity(name = "UserTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String encodedPassword;

    @Lob
    private Blob imageFile;

    private boolean image;

    @ManyToMany
    @JoinTable(name = "user_course")
    private List<Course> courses;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public User() {
        // Empty constructor required by JPA
    }

    public User(String name, String email, String encodedPassword, Blob imageFile, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.imageFile = imageFile;
        this.roles = List.of(roles);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
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

    public void setImage(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
