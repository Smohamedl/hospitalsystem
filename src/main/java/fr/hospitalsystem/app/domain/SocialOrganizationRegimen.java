package fr.hospitalsystem.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A SocialOrganizationRegimen.
 */
@Entity
@Table(name = "social_organization_regimen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "socialorganizationregimen")
public class SocialOrganizationRegimen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "100")
    @Column(name = "percentage", nullable = false)
    private Double percentage;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("socialOrganizationRegimen")
    private SocialOrganization socialOrganization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentage() {
        return percentage;
    }

    public SocialOrganizationRegimen percentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public SocialOrganization getSocialOrganization() {
        return socialOrganization;
    }

    public SocialOrganizationRegimen socialOrganization(SocialOrganization socialOrganization) {
        this.socialOrganization = socialOrganization;
        return this;
    }

    public void setSocialOrganization(SocialOrganization socialOrganization) {
        this.socialOrganization = socialOrganization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialOrganizationRegimen)) {
            return false;
        }
        return id != null && id.equals(((SocialOrganizationRegimen) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SocialOrganizationRegimen{" +
            "id=" + getId() +
            ", percentage=" + getPercentage() +
            "}";
    }
}
