package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Patient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Patient} entity.
 */
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {
}
