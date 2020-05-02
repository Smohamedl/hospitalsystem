package fr.hospitalsystem.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SocialOrganization.
 */
@Entity
@Table(name = "social_organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "socialorganization")
public class SocialOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "socialOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SocialOrganizationRegimen> socialOrganizationRegimen = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SocialOrganization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SocialOrganizationRegimen> getSocialOrganizationRegimen() {
        return socialOrganizationRegimen;
    }

    public SocialOrganization socialOrganizationRegimen(Set<SocialOrganizationRegimen> socialOrganizationRegimen) {
        this.socialOrganizationRegimen = socialOrganizationRegimen;
        return this;
    }

    public SocialOrganization addSocialOrganizationRegimen(SocialOrganizationRegimen socialOrganizationRegimen) {
        this.socialOrganizationRegimen.add(socialOrganizationRegimen);
        socialOrganizationRegimen.setSocialOrganization(this);
        return this;
    }

    public SocialOrganization removeSocialOrganizationRegimen(SocialOrganizationRegimen socialOrganizationRegimen) {
        this.socialOrganizationRegimen.remove(socialOrganizationRegimen);
        socialOrganizationRegimen.setSocialOrganization(null);
        return this;
    }

    public void setSocialOrganizationRegimen(Set<SocialOrganizationRegimen> socialOrganizationRegimen) {
        this.socialOrganizationRegimen = socialOrganizationRegimen;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialOrganization)) {
            return false;
        }
        return id != null && id.equals(((SocialOrganization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SocialOrganization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
