package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.MedicalService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MedicalService} entity.
 */
public interface MedicalServiceSearchRepository extends ElasticsearchRepository<MedicalService, Long> {
}
