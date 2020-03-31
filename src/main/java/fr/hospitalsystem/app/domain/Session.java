package fr.hospitalsystem.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

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
    private Integer totalCash;

    @NotNull
    @Column(name = "total_pc", nullable = false)
    private Integer totalPC;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total;

    @NotNull
    @Column(name = "total_check", nullable = false)
    private Integer totalCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    private User jhi_user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalCash() {
        return totalCash;
    }

    public Session totalCash(Integer totalCash) {
        this.totalCash = totalCash;
        return this;
    }

    public void setTotalCash(Integer totalCash) {
        this.totalCash = totalCash;
    }

    public Integer getTotalPC() {
        return totalPC;
    }

    public Session totalPC(Integer totalPC) {
        this.totalPC = totalPC;
        return this;
    }

    public void setTotalPC(Integer totalPC) {
        this.totalPC = totalPC;
    }

    public Integer getTotal() {
        return total;
    }

    public Session total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalCheck() {
        return totalCheck;
    }

    public Session totalCheck(Integer totalCheck) {
        this.totalCheck = totalCheck;
        return this;
    }

    public void setTotalCheck(Integer totalCheck) {
        this.totalCheck = totalCheck;
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
            "}";
    }
}
