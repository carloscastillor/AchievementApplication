package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PersonalizedAchievement.
 */
@Entity
@Table(name = "personalized_achievement")
public class PersonalizedAchievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "videogames" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Achievement achievement;

    @OneToMany(mappedBy = "personalizedAchievement")
    @JsonIgnoreProperties(value = { "achievement", "personalizedAchievement" }, allowSetters = true)
    private Set<Videogame> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalizedAchievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Achievement getAchievement() {
        return this.achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public PersonalizedAchievement achievement(Achievement achievement) {
        this.setAchievement(achievement);
        return this;
    }

    public Set<Videogame> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Videogame> videogames) {
        if (this.users != null) {
            this.users.forEach(i -> i.setPersonalizedAchievement(null));
        }
        if (videogames != null) {
            videogames.forEach(i -> i.setPersonalizedAchievement(this));
        }
        this.users = videogames;
    }

    public PersonalizedAchievement users(Set<Videogame> videogames) {
        this.setUsers(videogames);
        return this;
    }

    public PersonalizedAchievement addUser(Videogame videogame) {
        this.users.add(videogame);
        videogame.setPersonalizedAchievement(this);
        return this;
    }

    public PersonalizedAchievement removeUser(Videogame videogame) {
        this.users.remove(videogame);
        videogame.setPersonalizedAchievement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalizedAchievement)) {
            return false;
        }
        return id != null && id.equals(((PersonalizedAchievement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalizedAchievement{" +
            "id=" + getId() +
            "}";
    }
}
