package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Provider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Provider} entity.
 */
public interface ProviderSearchRepository extends ElasticsearchRepository<Provider, Long> {
}
