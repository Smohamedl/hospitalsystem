package fr.hospitalsystem.app.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private MedicalService medicalService;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private Actype actype;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("acts")
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(unique = true)
    @JsonBackReference
    private ReceiptAct receiptAct;


    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "act_actypes", joinColumns = @JoinColumn(name = "act_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "actypes_id", referencedColumnName = "id"))
    private Set<Actype> actypes = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("acts")
    private PaymentMethod paymentMethod;

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
  
    public Set<Actype> getActypes() {
        return actypes;
    }

    public Act actypes(Set<Actype> actypes) {
        this.actypes = actypes;
        return this;
    }

    public Act addActypes(Actype actype) {
        this.actypes.add(actype);
        // actype.getActs().add(this);
        return this;
    }

    public Act removeActypes(Actype actype) {
        this.actypes.remove(actype);
        // actype.getActs().remove(this);
        return this;
    }

    public void setActypes(Set<Actype> actypes) {
        this.actypes = actypes;
    }
  
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Act paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        return "Act{" +
            "id=" + getId() +
            ", patientName='" + getPatientName() + "'" +
            "}";
    }
}