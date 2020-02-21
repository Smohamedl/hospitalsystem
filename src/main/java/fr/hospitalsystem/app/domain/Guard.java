package fr.hospitalsystem.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

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
    @Column(name = "pay", nullable = false)
    private Float pay;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPay() {
        return pay;
    }

    public Guard pay(Float pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(Float pay) {
        this.pay = pay;
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
        return "Guard{" +
            "id=" + getId() +
            ", pay=" + getPay() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
