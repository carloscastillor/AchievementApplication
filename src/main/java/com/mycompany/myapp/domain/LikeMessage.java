package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A LikeMessage.
 */
@Entity
@Table(name = "like_message")
public class LikeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_like")
    private Boolean like;

    @OneToMany(mappedBy = "likeMessage")
    @JsonIgnoreProperties(value = { "likeMessage" }, allowSetters = true)
    private Set<Message> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLike() {
        return this.like;
    }

    public LikeMessage like(Boolean like) {
        this.setLike(like);
        return this;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Set<Message> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Message> messages) {
        if (this.users != null) {
            this.users.forEach(i -> i.setLikeMessage(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setLikeMessage(this));
        }
        this.users = messages;
    }

    public LikeMessage users(Set<Message> messages) {
        this.setUsers(messages);
        return this;
    }

    public LikeMessage addUser(Message message) {
        this.users.add(message);
        message.setLikeMessage(this);
        return this;
    }

    public LikeMessage removeUser(Message message) {
        this.users.remove(message);
        message.setLikeMessage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeMessage)) {
            return false;
        }
        return id != null && id.equals(((LikeMessage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeMessage{" +
            "id=" + getId() +
            ", like='" + getLike() + "'" +
            "}";
    }
}
