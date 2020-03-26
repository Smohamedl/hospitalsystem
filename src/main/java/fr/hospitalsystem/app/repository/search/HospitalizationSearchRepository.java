package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Hospitalization;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Hospitalization} entity.
 */
public interface HospitalizationSearchRepository extends ElasticsearchRepository<Hospitalization, Long> {
}
