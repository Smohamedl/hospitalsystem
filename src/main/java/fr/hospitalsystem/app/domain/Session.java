package fr.hospitalsystem.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "session")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "total_cash", nullable = false)
    private Double totalCash;

    @NotNull
    @Column(name = "total_pc", nullable = false)
    private Double totalPC;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @NotNull
    @Column(name = "total_check", nullable = false)
    private Double totalCheck;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String created_by;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant created_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("sessions")
    private User jhi_user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalCash() {
        return totalCash;
    }

    public Session totalCash(Double totalCash) {
        this.totalCash = totalCash;
        return this;
    }

    public void setTotalCash(Double totalCash) {
        this.totalCash = totalCash;
    }

    public Double getTotalPC() {
        return totalPC;
    }

    public Session totalPC(Double totalPC) {
        this.totalPC = totalPC;
        return this;
    }

    public void setTotalPC(Double totalPC) {
        this.totalPC = totalPC;
    }

    public Double getTotal() {
        return total;
    }

    public Session total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotalCheck() {
        return totalCheck;
    }

    public Session totalCheck(Double totalCheck) {
        this.totalCheck = totalCheck;
        return this;
    }

    public void setTotalCheck(Double totalCheck) {
        this.totalCheck = totalCheck;
    }

    public String getCreated_by() {
        return created_by;
    }

    public Session created_by(String created_by) {
        this.created_by = created_by;
        return this;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Instant getCreated_date() {
        return created_date;
    }

    public Session created_date(Instant created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(Instant created_date) {
        this.created_date = created_date;
    }

    public User getJhi_user() {
        return jhi_user;
    }

    public Session jhi_user(User jhi_user) {
        this.jhi_user = jhi_user;
        return this;
    }

    public void setJhi_user(User jhi_user) {
        this.jhi_user = jhi_user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return id != null && id.equals(((Session) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", totalCash=" + getTotalCash() +
            ", totalPC=" + getTotalPC() +
            ", total=" + getTotal() +
            ", totalCheck=" + getTotalCheck() +
            ", created_by='" + getCreated_by() + "'" +
            ", created_date='" + getCreated_date() + "'" +
            "}";
    }
}
