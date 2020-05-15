package fr.hospitalsystem.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A SocialOrganizationDetails.
 */
@Entity
@Table(name = "social_organization_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "socialorganizationdetails")
public class SocialOrganizationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Min(value = 1L)
    @Column(name = "registration_number", nullable = false, unique = true)
    private Long registrationNumber;

    @NotNull
    @Size(min = 1)
    @Column(name = "matricule_number", nullable = false, unique = true)
    private String matriculeNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("socialOrganizationDetails")
    private SocialOrganizationRegimen socialOrganizationRegimen;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistrationNumber() {
        return registrationNumber;
    }

    public SocialOrganizationDetails registrationNumber(Long registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }

    public void setRegistrationNumber(Long registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMatriculeNumber() {
        return matriculeNumber;
    }

    public SocialOrganizationDetails matriculeNumber(String matriculeNumber) {
        this.matriculeNumber = matriculeNumber;
        return this;
    }

    public void setMatriculeNumber(String matriculeNumber) {
        this.matriculeNumber = matriculeNumber;
    }

    public SocialOrganizationRegimen getSocialOrganizationRegimen() {
        return socialOrganizationRegimen;
    }

    public SocialOrganizationDetails socialOrganizationRegimen(SocialOrganizationRegimen socialOrganizationRegimen) {
        this.socialOrganizationRegimen = socialOrganizationRegimen;
        return this;
    }

    public void setSocialOrganizationRegimen(SocialOrganizationRegimen socialOrganizationRegimen) {
        this.socialOrganizationRegimen = socialOrganizationRegimen;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialOrganizationDetails)) {
            return false;
        }
        return id != null && id.equals(((SocialOrganizationDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SocialOrganizationDetails{" +
            "id=" + getId() +
            ", registrationNumber=" + getRegistrationNumber() +
            ", matriculeNumber='" + getMatriculeNumber() + "'" +
            "}";
    }
}
