package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.SocialOrganizationDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SocialOrganizationDetails} entity.
 */
public interface SocialOrganizationDetailsSearchRepository extends ElasticsearchRepository<SocialOrganizationDetails, Long> {
}
