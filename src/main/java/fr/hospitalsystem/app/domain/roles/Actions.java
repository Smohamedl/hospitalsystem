package fr.hospitalsystem.app.domain.roles;

import fr.hospitalsystem.app.domain.Authority;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "jhi_actions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Actions implements Serializable  {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actions)) {
            return false;
        }
        return Objects.equals(name, ((Actions) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Actions{" +
            "name='" + name + '\'' +
            "}";
    }
}
