package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Guard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Guard} entity.
 */
public interface GuardSearchRepository extends ElasticsearchRepository<Guard, Long> {
}
