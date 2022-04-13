package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Videogame.
 */
@Entity
@Table(name = "videogame")
public class Videogame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "videogames" }, allowSetters = true)
    private Achievement achievement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "achievement", "users" }, allowSetters = true)
    private PersonalizedAchievement personalizedAchievement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Videogame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Videogame name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Achievement getAchievement() {
        return this.achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public Videogame achievement(Achievement achievement) {
        this.setAchievement(achievement);
        return this;
    }

    public PersonalizedAchievement getPersonalizedAchievement() {
        return this.personalizedAchievement;
    }

    public void setPersonalizedAchievement(PersonalizedAchievement personalizedAchievement) {
        this.personalizedAchievement = personalizedAchievement;
    }

    public Videogame personalizedAchievement(PersonalizedAchievement personalizedAchievement) {
        this.setPersonalizedAchievement(personalizedAchievement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Videogame)) {
            return false;
        }
        return id != null && id.equals(((Videogame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Videogame{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
