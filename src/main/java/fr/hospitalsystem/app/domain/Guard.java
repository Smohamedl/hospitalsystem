package fr.hospitalsystem.app.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Guard.
 */
@Entity
@Table(name = "guard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "guard")
public class Guard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("guards")
    private GuardSchedule guardSchedule;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("guards")
    private MedicalService doctorMedicalService;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("guards")
    private Doctor doctor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Guard date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public GuardSchedule getGuardSchedule() {
        return guardSchedule;
    }

    public Guard guardSchedule(GuardSchedule guardSchedule) {
        this.guardSchedule = guardSchedule;
        return this;
    }

    public void setGuardSchedule(GuardSchedule guardSchedule) {
        this.guardSchedule = guardSchedule;
    }

    public MedicalService getDoctorMedicalService() {
        return doctorMedicalService;
    }

    public Guard doctorMedicalService(MedicalService medicalService) {
        this.doctorMedicalService = medicalService;
        return this;
    }

    public void setDoctorMedicalService(MedicalService medicalService) {
        this.doctorMedicalService = medicalService;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Guard doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Guard)) {
            return false;
        }
        return id != null && id.equals(((Guard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Guard{" + "id=" + getId() + ", date='" + getDate() + "'" + "}";
    }
}
