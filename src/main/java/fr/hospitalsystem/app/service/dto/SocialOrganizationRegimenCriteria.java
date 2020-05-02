package fr.hospitalsystem.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.hospitalsystem.app.domain.SocialOrganizationRegimen} entity. This class is used
 * in {@link fr.hospitalsystem.app.web.rest.SocialOrganizationRegimenResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /social-organization-regimen?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SocialOrganizationRegimenCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter percentage;

    private LongFilter socialOrganizationId;

    public SocialOrganizationRegimenCriteria(){
    }

    public SocialOrganizationRegimenCriteria(SocialOrganizationRegimenCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.percentage = other.percentage == null ? null : other.percentage.copy();
        this.socialOrganizationId = other.socialOrganizationId == null ? null : other.socialOrganizationId.copy();
    }

    @Override
    public SocialOrganizationRegimenCriteria copy() {
        return new SocialOrganizationRegimenCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getPercentage() {
        return percentage;
    }

    public void setPercentage(DoubleFilter percentage) {
        this.percentage = percentage;
    }

    public LongFilter getSocialOrganizationId() {
        return socialOrganizationId;
    }

    public void setSocialOrganizationId(LongFilter socialOrganizationId) {
        this.socialOrganizationId = socialOrganizationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SocialOrganizationRegimenCriteria that = (SocialOrganizationRegimenCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(percentage, that.percentage) &&
            Objects.equals(socialOrganizationId, that.socialOrganizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        percentage,
        socialOrganizationId
        );
    }

    @Override
    public String toString() {
        return "SocialOrganizationRegimenCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (percentage != null ? "percentage=" + percentage + ", " : "") +
                (socialOrganizationId != null ? "socialOrganizationId=" + socialOrganizationId + ", " : "") +
            "}";
    }

}
