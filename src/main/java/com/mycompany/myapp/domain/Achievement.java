package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Achievement.
 */
@Entity
@Table(name = "achievement")
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "videogame")
    private String videogame;

    @Column(name = "completed")
    private Boolean completed;

    @OneToMany(mappedBy = "achievement")
    @JsonIgnoreProperties(value = { "achievement", "personalizedAchievement" }, allowSetters = true)
    private Set<Videogame> videogames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Achievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Achievement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Achievement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideogame() {
        return this.videogame;
    }

    public Achievement videogame(String videogame) {
        this.setVideogame(videogame);
        return this;
    }

    public void setVideogame(String videogame) {
        this.videogame = videogame;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public Achievement completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Set<Videogame> getVideogames() {
        return this.videogames;
    }

    public void setVideogames(Set<Videogame> videogames) {
        if (this.videogames != null) {
            this.videogames.forEach(i -> i.setAchievement(null));
        }
        if (videogames != null) {
            videogames.forEach(i -> i.setAchievement(this));
        }
        this.videogames = videogames;
    }

    public Achievement videogames(Set<Videogame> videogames) {
        this.setVideogames(videogames);
        return this;
    }

    public Achievement addVideogame(Videogame videogame) {
        this.videogames.add(videogame);
        videogame.setAchievement(this);
        return this;
    }

    public Achievement removeVideogame(Videogame videogame) {
        this.videogames.remove(videogame);
        videogame.setAchievement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Achievement)) {
            return false;
        }
        return id != null && id.equals(((Achievement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Achievement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", videogame='" + getVideogame() + "'" +
            ", completed='" + getCompleted() + "'" +
            "}";
    }
}
