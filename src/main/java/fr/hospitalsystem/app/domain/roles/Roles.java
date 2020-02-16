package fr.hospitalsystem.app.domain.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.hospitalsystem.app.domain.Authority;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jhi_roles")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_roles_actions",
        joinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "actions_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Actions> actions = new HashSet<>();

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public Set<Actions> getActions(){
        return this.actions;
    }

    public void setActions(Set<Actions> actions){
        this.actions = actions;
    }
}
