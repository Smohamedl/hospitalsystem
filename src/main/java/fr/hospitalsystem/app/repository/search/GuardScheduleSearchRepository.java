package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.GuardSchedule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link GuardSchedule} entity.
 */
public interface GuardScheduleSearchRepository extends ElasticsearchRepository<GuardSchedule, Long> {
}
