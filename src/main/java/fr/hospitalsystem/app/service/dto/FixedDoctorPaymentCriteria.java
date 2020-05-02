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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link fr.hospitalsystem.app.domain.FixedDoctorPayment} entity. This class is used
 * in {@link fr.hospitalsystem.app.web.rest.FixedDoctorPaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fixed-doctor-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FixedDoctorPaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter paid;

    private LocalDateFilter date;

    private StringFilter reference;

    private LongFilter doctorId;

    public FixedDoctorPaymentCriteria(){
    }

    public FixedDoctorPaymentCriteria(FixedDoctorPaymentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.paid = other.paid == null ? null : other.paid.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
    }

    @Override
    public FixedDoctorPaymentCriteria copy() {
        return new FixedDoctorPaymentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getReference() {
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FixedDoctorPaymentCriteria that = (FixedDoctorPaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(date, that.date) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paid,
        date,
        reference,
        doctorId
        );
    }

    @Override
    public String toString() {
        return "FixedDoctorPaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (reference != null ? "reference=" + reference + ", " : "") +
                (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            "}";
    }

}
