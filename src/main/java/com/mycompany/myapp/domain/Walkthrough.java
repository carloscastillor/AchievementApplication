package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Walkthrough.
 */
@Entity
@Table(name = "walkthrough")
public class Walkthrough implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private Resource resource;

    @JsonIgnoreProperties(value = { "videogames" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Achievement user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Walkthrough id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Walkthrough title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Walkthrough description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Walkthrough resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    public Achievement getUser() {
        return this.user;
    }

    public void setUser(Achievement achievement) {
        this.user = achievement;
    }

    public Walkthrough user(Achievement achievement) {
        this.setUser(achievement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Walkthrough)) {
            return false;
        }
        return id != null && id.equals(((Walkthrough) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Walkthrough{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
