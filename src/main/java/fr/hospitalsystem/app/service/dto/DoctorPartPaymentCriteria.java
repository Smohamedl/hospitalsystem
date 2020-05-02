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
 * Criteria class for the {@link fr.hospitalsystem.app.domain.DoctorPartPayment} entity. This class is used
 * in {@link fr.hospitalsystem.app.web.rest.DoctorPartPaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doctor-part-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DoctorPartPaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter total;

    private StringFilter reference;

    private LocalDateFilter date;

    private LongFilter doctorId;

    public DoctorPartPaymentCriteria(){
    }

    public DoctorPartPaymentCriteria(DoctorPartPaymentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
    }

    @Override
    public DoctorPartPaymentCriteria copy() {
        return new DoctorPartPaymentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getTotal() {
        return total;
    }

    public void setTotal(DoubleFilter total) {
        this.total = total;
    }

    public StringFilter getReference() {
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
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
        final DoctorPartPaymentCriteria that = (DoctorPartPaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(total, that.total) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(date, that.date) &&
            Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        total,
        reference,
        date,
        doctorId
        );
    }

    @Override
    public String toString() {
        return "DoctorPartPaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (reference != null ? "reference=" + reference + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            "}";
    }

}
