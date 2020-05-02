package fr.hospitalsystem.app.repository.search;
import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DoctorMonthlyPayment} entity.
 */
public interface DoctorMonthlyPaymentSearchRepository extends ElasticsearchRepository<DoctorMonthlyPayment, Long> {
}
