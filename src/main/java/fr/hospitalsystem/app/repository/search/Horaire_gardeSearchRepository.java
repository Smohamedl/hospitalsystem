package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.Horaire_garde;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Horaire_garde} entity.
 */
public interface Horaire_gardeSearchRepository extends ElasticsearchRepository<Horaire_garde, Long> {
}
