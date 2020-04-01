package fr.hospitalsystem.app.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Act.
 */
@Entity
@Table(name = "act")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "act")
public class Act implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private MedicalService medicalService;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private Actype actype;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("acts")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(unique = true)
    private ReceiptAct receiptAct;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public Act patientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public MedicalService getMedicalService() {
        return medicalService;
    }

    public Act medicalService(MedicalService medicalService) {
        this.medicalService = medicalService;
        return this;
    }

    public void setMedicalService(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    public Actype getActype() {
        return actype;
    }

    public Act actype(Actype actype) {
        this.actype = actype;
        return this;
    }

    public void setActype(Actype actype) {
        this.actype = actype;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Act doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public Act patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ReceiptAct getReceiptAct() {
        return receiptAct;
    }

    public Act receiptAct(ReceiptAct receiptAct) {
        this.receiptAct = receiptAct;
        return this;
    }

    public void setReceiptAct(ReceiptAct receiptAct) {
        this.receiptAct = receiptAct;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Act)) {
            return false;
        }
        return id != null && id.equals(((Act) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Act{" + "id=" + getId() + ", patientName='" + getPatientName() + "'" + "}";
    }
}
