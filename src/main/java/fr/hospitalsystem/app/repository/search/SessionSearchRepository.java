package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Session;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Session} entity.
 */
public interface SessionSearchRepository extends ElasticsearchRepository<Session, Long> {
}
