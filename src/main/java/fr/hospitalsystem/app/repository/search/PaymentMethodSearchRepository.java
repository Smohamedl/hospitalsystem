package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.PaymentMethod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentMethod} entity.
 */
public interface PaymentMethodSearchRepository extends ElasticsearchRepository<PaymentMethod, Long> {
}
