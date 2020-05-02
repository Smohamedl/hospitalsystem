package fr.hospitalsystem.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties("orders")
    private Provider provider;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    private Set<QuantityPrice> quantityPrices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public Order provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Set<QuantityPrice> getQuantityPrices() {
        return quantityPrices;
    }

    public Order quantityPrices(Set<QuantityPrice> quantityPrices) {
        this.quantityPrices = quantityPrices;
        return this;
    }

    public Order addQuantityPrice(QuantityPrice quantityPrice) {
        this.quantityPrices.add(quantityPrice);
        quantityPrice.setOrder(this);
        return this;
    }

    public Order removeQuantityPrice(QuantityPrice quantityPrice) {
        this.quantityPrices.remove(quantityPrice);
        quantityPrice.setOrder(null);
        return this;
    }

    public void setQuantityPrices(Set<QuantityPrice> quantityPrices) {
        this.quantityPrices = quantityPrices;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", provider=" + provider +
            ", quantityPrices=" + quantityPrices.toString() +
            '}';
    }
}
