package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.DoctorPartPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DoctorPartPayment} entity.
 */
public interface DoctorPartPaymentSearchRepository extends ElasticsearchRepository<DoctorPartPayment, Long> {
}
