package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Act;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Act} entity.
 */
public interface ActSearchRepository extends ElasticsearchRepository<Act, Long> {
}
