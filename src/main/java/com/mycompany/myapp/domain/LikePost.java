package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A LikePost.
 */
@Entity
@Table(name = "like_post")
public class LikePost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_like")
    private Boolean like;

    @OneToMany(mappedBy = "likePost")
    @JsonIgnoreProperties(value = { "message", "likePost" }, allowSetters = true)
    private Set<Post> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikePost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLike() {
        return this.like;
    }

    public LikePost like(Boolean like) {
        this.setLike(like);
        return this;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Set<Post> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Post> posts) {
        if (this.users != null) {
            this.users.forEach(i -> i.setLikePost(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setLikePost(this));
        }
        this.users = posts;
    }

    public LikePost users(Set<Post> posts) {
        this.setUsers(posts);
        return this;
    }

    public LikePost addUser(Post post) {
        this.users.add(post);
        post.setLikePost(this);
        return this;
    }

    public LikePost removeUser(Post post) {
        this.users.remove(post);
        post.setLikePost(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikePost)) {
            return false;
        }
        return id != null && id.equals(((LikePost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikePost{" +
            "id=" + getId() +
            ", like='" + getLike() + "'" +
            "}";
    }
}
