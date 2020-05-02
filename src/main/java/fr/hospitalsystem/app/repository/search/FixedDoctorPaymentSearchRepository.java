package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FixedDoctorPayment} entity.
 */
public interface FixedDoctorPaymentSearchRepository extends ElasticsearchRepository<FixedDoctorPayment, Long> {
}
