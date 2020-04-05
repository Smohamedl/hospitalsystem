package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.QuantityPrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link QuantityPrice} entity.
 */
public interface QuantityPriceSearchRepository extends ElasticsearchRepository<QuantityPrice, Long> {
}
