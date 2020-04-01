package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.SocialOrganization;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SocialOrganization} entity.
 */
public interface SocialOrganizationSearchRepository extends ElasticsearchRepository<SocialOrganization, Long> {
}
