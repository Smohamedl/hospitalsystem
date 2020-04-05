package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Stock} entity.
 */
public interface StockSearchRepository extends ElasticsearchRepository<Stock, Long> {
}
