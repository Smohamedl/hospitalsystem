package fr.hospitalsystem.app.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * A ReceiptAct.
 */
@Entity
@Table(name = "receipt_act")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "receiptact")
public class ReceiptAct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "paid_doctor")
    private Boolean paidDoctor;

    @Column(name = "date")
    private LocalDate date;

    @OneToOne(mappedBy = "receiptAct", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JsonBackReference
    private Act act;

    /*
     * @OneToOne(fetch = FetchType.EAGER)
     * 
     * @JoinColumn(unique = true) private Act act;
     */

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public ReceiptAct total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean isPaid() {
        return paid;
    }

    public ReceiptAct paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean isPaidDoctor() {
        return paidDoctor;
    }

    public ReceiptAct paidDoctor(Boolean paidDoctor) {
        this.paidDoctor = paidDoctor;
        return this;
    }

    public void setPaidDoctor(Boolean paidDoctor) {
        this.paidDoctor = paidDoctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReceiptAct date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /*
     * public Act getAct() { return act; }
     * 
     * public ReceiptAct act(Act act) { this.act = act; return this; }
     * 
     * public void setAct(Act act) { this.act = act; }
     */
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptAct)) {
            return false;
        }
        return id != null && id.equals(((ReceiptAct) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ReceiptAct{" + "id=" + getId() + ", total=" + getTotal() + ", paid='" + isPaid() + "'" + ", paidDoctor='" + isPaidDoctor() + "'"
                + ", date='" + getDate() + "'" + "}";
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

}
