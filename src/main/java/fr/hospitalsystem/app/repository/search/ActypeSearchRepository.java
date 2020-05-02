package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Actype;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Actype} entity.
 */
public interface ActypeSearchRepository extends ElasticsearchRepository<Actype, Long> {
}
