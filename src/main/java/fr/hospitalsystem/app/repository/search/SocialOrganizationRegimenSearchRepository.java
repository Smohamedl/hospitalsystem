package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.SocialOrganizationRegimen;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SocialOrganizationRegimen} entity.
 */
public interface SocialOrganizationRegimenSearchRepository extends ElasticsearchRepository<SocialOrganizationRegimen, Long> {
}
