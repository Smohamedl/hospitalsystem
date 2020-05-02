package fr.hospitalsystem.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A GuardSchedule.
 */
@Entity
@Table(name = "guard_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "guardschedule")
public class GuardSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "payement", nullable = false)
    private Double payement;

    @NotNull
    @Column(name = "start", nullable = false)
    private Integer start;

    @NotNull
    @Column(name = "end", nullable = false)
    private Integer end;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPayement() {
        return payement;
    }

    public GuardSchedule payement(Double payement) {
        this.payement = payement;
        return this;
    }

    public void setPayement(Double payement) {
        this.payement = payement;
    }

    public Integer getStart() {
        return start;
    }

    public GuardSchedule start(Integer start) {
        this.start = start;
        return this;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public GuardSchedule end(Integer end) {
        this.end = end;
        return this;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public GuardSchedule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuardSchedule)) {
            return false;
        }
        return id != null && id.equals(((GuardSchedule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuardSchedule{" +
            "id=" + getId() +
            ", payement=" + getPayement() +
            ", start=" + getStart() +
            ", end=" + getEnd() +
            ", name='" + getName() + "'" +
            "}";
    }
}
