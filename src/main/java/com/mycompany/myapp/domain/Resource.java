package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url_source")
    private String urlSource;

    @Column(name = "resource_type")
    private String resourceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Resource title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Resource description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlSource() {
        return this.urlSource;
    }

    public Resource urlSource(String urlSource) {
        this.setUrlSource(urlSource);
        return this;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public Resource resourceType(String resourceType) {
        this.setResourceType(resourceType);
        return this;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", urlSource='" + getUrlSource() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            "}";
    }
}
