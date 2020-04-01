package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.ReceiptAct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ReceiptAct} entity.
 */
public interface ReceiptActSearchRepository extends ElasticsearchRepository<ReceiptAct, Long> {
}
